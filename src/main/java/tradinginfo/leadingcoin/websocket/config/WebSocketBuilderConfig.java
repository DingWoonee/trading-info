package tradinginfo.leadingcoin.websocket.config;

import java.util.EnumMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tradinginfo.leadingcoin.model.Exchange;
import tradinginfo.leadingcoin.websocket.builder.BithumbPayloadBuilder;
import tradinginfo.leadingcoin.websocket.builder.PayloadBuilder;
import tradinginfo.leadingcoin.websocket.builder.UpbitPayloadBuilder;

@Configuration
public class WebSocketBuilderConfig {

    @Bean
    public Map<Exchange, PayloadBuilder> webSocketPayloadBuilders() {
        Map<Exchange, PayloadBuilder> map = new EnumMap<>(Exchange.class);
        map.put(Exchange.UPBIT, upbitPayloadBuilder());
        map.put(Exchange.BITHUMB, bithumbPayloadBuilder());
        return map;
    }

    @Bean
    public PayloadBuilder upbitPayloadBuilder() {
        return new UpbitPayloadBuilder();
    }

    @Bean
    public PayloadBuilder bithumbPayloadBuilder() {
        return new BithumbPayloadBuilder();
    }
}
