package tradinginfo.leadingcoin.repository;

import java.time.Duration;
import java.time.Instant;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;
import org.springframework.stereotype.Component;
import tradinginfo.leadingcoin.model.CoinKey;
import tradinginfo.leadingcoin.model.Trade;
import tradinginfo.leadingcoin.repository.dto.TradeAmountDto;

@Component
public class InMemoryTradeRepository implements TradeRepository {

    private final int MINUTE_RANGE = 5;

    // 거래 데이터 큐
    private final Map<CoinKey, Deque<Trade>> tradeData = new HashMap<>();
    // 최근 5분 누적 거래량 데이터
    private final Map<CoinKey, DoubleAdder> tradeAmountSumData = new HashMap<>();

    @Override
    public void save(Trade trade) {
        addTrade(trade);
        addVolumeSum(trade.key(), trade.tradeAmount());
    }

    @Override
    public List<TradeAmountDto> getTradeAmounts() {
        Instant now = Instant.now();
        return tradeAmountSumData.keySet().stream()
                .peek(c -> evictOld(c, now))
                .map(c -> new TradeAmountDto(
                        c.exchange(),
                        c.market(),
                        getVolumeSum(c).longValue(),
                        now.toEpochMilli()))
                .toList();

    }

    private void addTrade(Trade trade) {
        getTradeQueue(trade.key()).offer(trade);
    }

    private void evictOld(CoinKey key, Instant time) {
        Instant cut = threshold(time);
        Deque<Trade> data = getTradeQueue(key);
        while (!data.isEmpty() && data.peek().timestamp().isBefore(cut)) {
            Trade old = data.poll();
            addVolumeSum(key, -old.tradeAmount());
        }
    }

    private void addVolumeSum(CoinKey key, double volume) {
        getVolumeSum(key).add(volume);
    }

    private Deque<Trade> getTradeQueue(CoinKey key) {
        return tradeData.computeIfAbsent(key, k -> new ConcurrentLinkedDeque<>());
    }

    private DoubleAdder getVolumeSum(CoinKey key) {
        return tradeAmountSumData.computeIfAbsent(key, k -> new DoubleAdder());
    }

    private Instant threshold(Instant now) {
        return now.minus(Duration.ofMinutes(MINUTE_RANGE));
    }
}
