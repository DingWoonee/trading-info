let stompClient;

document.addEventListener('DOMContentLoaded', () => {
  fetch('/api/coins')
  .then(res => res.json())
  .then(json => {
    if (!json.isSuccess) {
      return console.error(json.message);
    }
    connectWebSocket()
    renderExchanges(json.data);
    initChart();
    setupListeners();
  });
});

function connectWebSocket() {
  const socket = new SockJS('/ws');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, () => console.log('WS connected'));
}

function renderExchanges(data) {
  const tabs = document.getElementById('tab-list');
  const panels = document.getElementById('tab-panels');
  data.forEach((ex, i) => {
    // 탭 생성
    const tab = document.createElement('li');
    tab.textContent = ex.name;
    tab.dataset.idx = i;
    if (i === 0) {
      tab.classList.add('active');
    }
    tabs.append(tab);

    // 패널 생성
    const panel = document.createElement('div');
    panel.className = 'tab-panel' + (i === 0 ? ' active' : '');
    panel.dataset.idx = i;

    const ul = document.createElement('ul');
    ul.className = 'coin-list';
    ex.coins.forEach(c => {
      const li = document.createElement('li');
      const cb = document.createElement('input');
      cb.type = 'checkbox';
      cb.value = `${ex.name}.${c.market}`;
      const lbl = document.createElement('label');
      lbl.append(cb, document.createTextNode(
          `${c.market.split('-')[0]}마켓 (${c.enName} ${c.krName})`));
      if (c.isWarning) {
        const warn = document.createElement('span');
        warn.className = 'coin-warning';
        warn.textContent = '⚠️';
        lbl.append(warn);
      }
      li.append(lbl);
      ul.append(li);
    });
    panel.append(ul);
    panels.append(panel);
  });

  // 탭 클릭
  tabs.addEventListener('click', e => {
    const li = e.target.closest('li');
    if (!li) {
      return;
    }
    const idx = li.dataset.idx;
    tabs.querySelectorAll('li').forEach(x => x.classList.remove('active'));
    panels.querySelectorAll('.tab-panel').forEach(
        x => x.classList.remove('active'));
    li.classList.add('active');
    panels.querySelector(`.tab-panel[data-idx="${idx}"]`).classList.add(
        'active');
  });
}

let chart;
const subscriptions = {};
const colorMap = {};

function initChart() {
  const canvas = document.getElementById('coinChart');
  const ctx = canvas.getContext('2d');

  chart = new Chart(ctx, {
    type: 'line',
    data: { datasets: [] },
    options: {
      responsive: true,
      maintainAspectRatio: false,

      // {x,y} 형태 자동 파싱
      parsing: {
        xAxisKey: 'x',
        yAxisKey: 'y'
      },

      scales: {
        x: {
          type: 'time',
          time: {
            parser: 'ISO',
            tooltipFormat: 'HH:mm:ss',
            displayFormats: {
              millisecond: 'HH:mm:ss',
              second:      'HH:mm:ss',
              minute:      'HH:mm:ss',
              hour:        'HH:mm:ss'
            }
          },
          bounds: 'data',   // data min/max 그대로
          grace: 0         // 여백 없이 딱 맞춤
        },
        y: {
          beginAtZero: true,
          title: { display: true, text: 'Trade Amount' }
        }
      },

      interaction: {
        mode: 'nearest',
        intersect: false
      },

      plugins: {
        tooltip: {
          enabled: true,
          callbacks: {
            title: items => {
              return new Date(items[0].parsed.x)
              .toLocaleTimeString();
            },
            label: ctx => {
              return `Amount: ${ctx.parsed.y.toLocaleString()}`;
            }
          }
        }
      },

      elements: {
        line: {
          tension: 0,
          showLine: true
        },
        point: {
          radius: 4,
          hoverRadius: 6
        }
      }
    }
  });
}

function setupListeners() {
  document.getElementById('exchange-container')
  .addEventListener('change', e => {
    if (e.target.type !== 'checkbox') return;
    const symbol = e.target.value; // e.g. "KRW-BTC"
    if (e.target.checked) subscribeSymbol(symbol);
    else unsubscribeSymbol(symbol);
  });
}

// 5) 구독 시작: 차트에 라인 추가 + STOMP 구독
function subscribeSymbol(symbol) {
  // 5-1) 차트에 데이터셋 추가
  addDataset(symbol);

  // 5-2) 실제 구독
  const topic = `/topic/trade.${symbol}`;
  console.log(topic);
  subscriptions[symbol] = stompClient.subscribe(topic, msg => {
    const { exchange, market, tradeAmount, timestamp } = JSON.parse(msg.body);
    appendDataToChart(`${exchange}.${market}`, tradeAmount, timestamp);
  });
}

// 6) 구독 해제: 차트에서 라인 제거 + STOMP 언구독
function unsubscribeSymbol(symbol) {
  // 6-1) STOMP 언구독
  if (subscriptions[symbol]) {
    subscriptions[symbol].unsubscribe();
    delete subscriptions[symbol];
  }
  // 6-2) 차트에서 데이터셋 제거
  removeDatasetFromChart(symbol);
}

// 7) 차트에 새로운 라인(dataset) 추가
function addDataset(symbol) {
  const color = randomColor();
  colorMap[symbol] = color;
  chart.data.datasets.push({
    label: symbol,
    data: [],               // 이후 {x:Date,y:Number} push
    borderColor: color,
    backgroundColor: color,
    fill: false,
    showLine: true,
    // 모든 포인트 그리기
    pointRadius: 0,
    pointHoverRadius: 6,
    pointBackgroundColor: color
  });
  chart.update();
}

// 8) 실시간 데이터 포인트 추가
function appendDataToChart(symbol, amount, timestampMs) {
  const ds = chart.data.datasets.find(d => d.label === symbol);
  if (!ds) return;
  ds.data.push({ x: timestampMs, y: amount });
  chart.update('none'); // 재렌더링만, 애니메이션 생략
}

// 9) 라인(dataset) 제거
function removeDatasetFromChart(symbol) {
  const idx = chart.data.datasets.findIndex(d => d.label === symbol);
  if (idx >= 0) {
    chart.data.datasets.splice(idx, 1);
    chart.update();
  }
}

// 유틸: 랜덤 헥스 색상 생성
function randomColor() {
  return '#' + Math.floor(Math.random()*0xFFFFFF).toString(16).padStart(6,'0');
}