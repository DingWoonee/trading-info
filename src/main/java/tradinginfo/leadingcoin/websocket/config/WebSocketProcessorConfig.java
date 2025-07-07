package tradinginfo.leadingcoin.websocket.config;

import java.util.EnumMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tradinginfo.leadingcoin.model.Exchange;
import tradinginfo.leadingcoin.repository.TradeRepository;
import tradinginfo.leadingcoin.websocket.processor.BithumbTradeProcessor;
import tradinginfo.leadingcoin.websocket.processor.TradeProcessor;
import tradinginfo.leadingcoin.websocket.processor.UpbitTradeProcessor;

@Configuration
@RequiredArgsConstructor
public class WebSocketProcessorConfig {

    private final TradeRepository tradeRepository;

    @Bean
    public Map<Exchange, TradeProcessor> webSocketTradeProcessors() {
        Map<Exchange, TradeProcessor> map = new EnumMap<>(Exchange.class);
        map.put(Exchange.UPBIT, upbitTradeProcessor());
        map.put(Exchange.BITHUMB, bithumbTradeProcessor());
        return map;
    }

    @Bean
    public TradeProcessor upbitTradeProcessor() {
        return new UpbitTradeProcessor(tradeRepository);
    }

    @Bean
    public TradeProcessor bithumbTradeProcessor() {
        return new BithumbTradeProcessor(tradeRepository);
    }
}
