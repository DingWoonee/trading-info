package tradinginfo.leadingcoin.websocket.client;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import tradinginfo.leadingcoin.model.Coin;
import tradinginfo.leadingcoin.model.Exchange;
import tradinginfo.leadingcoin.repository.ExchangeRegistry;
import tradinginfo.leadingcoin.utils.JsonUtils;
import tradinginfo.leadingcoin.websocket.builder.PayloadBuilder;
import tradinginfo.leadingcoin.websocket.processor.TradeProcessor;

@Slf4j
@Component
@DependsOn({"exchangeRegistry"}) // ExchangeRegistry 이후에 설정
@RequiredArgsConstructor
public class CoinWsClient {

    private final Map<Exchange, PayloadBuilder> builders;
    private final Map<Exchange, TradeProcessor> processors;

    private final WsClient wsClient;
    private final ExchangeRegistry exchangeRegistry;

    @PostConstruct
    public void init() {
        for (Exchange exchange : exchangeRegistry.getRegisteredExchanges()) {
            wsClient.connectWs(
                    exchange.getWsUrl(),
                    getPayload(exchange),
                    processors.get(exchange)
            );
        }
    }

    private String getPayload(Exchange exchange) {
        PayloadBuilder builder = builders.get(exchange);
        if (builder == null) {
            throw new RuntimeException(exchange.name() + " 거래소 용 PayloadBuilder가 등록되지 않았습니다.");
        }
        List<Coin> coins = exchangeRegistry.getCoins(exchange);

        return JsonUtils.objectToString(
                builder.buildPayload(coins)
        );
    }
}
