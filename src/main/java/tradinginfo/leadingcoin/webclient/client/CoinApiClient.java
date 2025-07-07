package tradinginfo.leadingcoin.webclient.client;

import static tradinginfo.leadingcoin.model.Exchange.BITHUMB;
import static tradinginfo.leadingcoin.model.Exchange.UPBIT;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import tradinginfo.leadingcoin.model.Coin;
import tradinginfo.leadingcoin.model.Exchange;
import tradinginfo.leadingcoin.webclient.dto.BithumbCoin;
import tradinginfo.leadingcoin.webclient.dto.UpbitCoin;
import tradinginfo.leadingcoin.webclient.mapper.CoinMapper;

@Component
@RequiredArgsConstructor
public class CoinApiClient {

    private final Map<Exchange, ParameterizedTypeReference<? extends List<?>>> typeRefs = Map.of(
            UPBIT,   new ParameterizedTypeReference<List<UpbitCoin>>() {},
            BITHUMB, new ParameterizedTypeReference<List<BithumbCoin>>() {}
    );
    private final Map<Exchange, CoinMapper<?>> mappers;

    private final ApiClient apiClient;

    @SuppressWarnings("unchecked")
    public <D> List<Coin> getSupportedCoins(Exchange exchange) {
        var typeRef = (ParameterizedTypeReference<List<D>>) typeRefs.get(exchange);
        var mapper = (CoinMapper<D>) mappers.get(exchange);
        if (mapper == null) {
            throw new RuntimeException(exchange.name() + " 거래소 용 Mapper가 등록되지 않았습니다.");
        }

        return apiClient.get(exchange.getApiUrl(), typeRef)
                .map(list -> list.stream()
                        .map(mapper::toCoin)
                        .toList())
                .block();
    }
}
