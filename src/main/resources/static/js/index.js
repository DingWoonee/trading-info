document.addEventListener('DOMContentLoaded', () => {
  const tabsContainer = document.getElementById('exchange-tabs');
  const tableContainer = document.getElementById('table-container');
  const tableBody = document.getElementById('table-body');
  let stompClient = null;
  let subscription = null;

  // 거래소 목록 로드
  fetch('/api/exchanges')
  .then(res => res.json())
  .then(data => {
    const exchanges = data.data;
    exchanges.forEach(ex => {
      const btn = document.createElement('button');
      btn.className = 'tab';
      btn.innerText = ex;
      btn.addEventListener('click', () => selectExchange(ex, btn));
      tabsContainer.appendChild(btn);
    });
    if (exchanges.length) selectExchange(exchanges[0], tabsContainer.firstChild);
  });

  function selectExchange(exchange, btn) {
    document.querySelectorAll('.tab').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    if (subscription) subscription.unsubscribe();

    if (!stompClient) {
      const socket = new SockJS('/ws');
      stompClient = Stomp.over(socket);
      stompClient.connect({}, () => subscribe(exchange));
    } else {
      subscribe(exchange);
    }

    tableContainer.style.display = 'block';
  }

  function subscribe(exchange) {
    subscription = stompClient.subscribe(`/topic/top10/${exchange}`, msg => {
      const data = JSON.parse(msg.body);
      renderTable(data);
    });
  }

  function renderTable(data) {
    tableBody.innerHTML = '';
    data.forEach((item, i) => {
      const row = document.createElement('tr');
      const rankTd = document.createElement('td'); rankTd.innerText = i + 1;
      const marketTd = document.createElement('td'); marketTd.innerText = item.market;
      const volumeTd = document.createElement('td'); volumeTd.innerText = Number(item.tradeAmount).toLocaleString();
      row.append(rankTd, marketTd, volumeTd);
      tableBody.appendChild(row);
    });
  }
});