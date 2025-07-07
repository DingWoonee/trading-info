package tradinginfo.leadingcoin.repository;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tradinginfo.leadingcoin.model.Coin;
import tradinginfo.leadingcoin.model.Exchange;
import tradinginfo.leadingcoin.webclient.client.CoinApiClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeRegistry {

    private final Map<Exchange, List<Coin>> registry = new ConcurrentHashMap<>();

    private final CoinApiClient coinApiClient;

    /**
     * 각 거래소에서 지원하는 코인 목록 데이터를 registry에 저장합니다.
     */
    @PostConstruct
    public void init() {
        Arrays.stream(Exchange.values())
                .forEach(exchange ->
                        registry.put(exchange, coinApiClient.getSupportedCoins(exchange)));
    }

    /**
     * 현재 registry에 등록된 거래소들을 반환합니다.
     */
    public Set<Exchange> getRegisteredExchanges() {
        return registry.keySet();
    }

    /**
     * 거래소에서 지원하는 코인 목록을 반환합니다.
     */
    public List<Coin> getCoins(Exchange exchange) {
        if (!registry.containsKey(exchange)) {
            throw new RuntimeException(exchange.name() + " 거래소가 registry에 등록되지 않았습니다.");
        }

        List<Coin> coins = registry.get(exchange);
        if (coins.isEmpty()) {
            throw new RuntimeException(exchange.name() + " 거래소의 지원 가능한 코인이 없습니다.");
        }
        return coins;
    }
}
