package tradinginfo.leadingcoin.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tradinginfo.leadingcoin.model.Exchange;
import tradinginfo.leadingcoin.service.dto.ExchangeWithCoins;
import tradinginfo.leadingcoin.repository.ExchangeRegistry;

@Service
@RequiredArgsConstructor
public class ExchangeService {

    private final ExchangeRegistry exchangeRegistry;

    public List<ExchangeWithCoins> getExchangesAndCoins() {
        return exchangeRegistry.getRegisteredExchanges().stream()
                .map(ex -> new ExchangeWithCoins(ex, exchangeRegistry.getCoins(ex)))
                .toList();
    }

    public List<Exchange> getExchanges() {
        return exchangeRegistry.getRegisteredExchanges().stream()
                .toList();
    }
}
