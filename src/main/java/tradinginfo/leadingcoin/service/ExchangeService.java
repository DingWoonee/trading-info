package tradinginfo.leadingcoin.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tradinginfo.leadingcoin.service.dto.ExchangeWithCoins;
import tradinginfo.leadingcoin.repository.ExchangeRegistry;

@Service
@RequiredArgsConstructor
public class ExchangeService {

    private final ExchangeRegistry exchangeRegistry;

    public List<ExchangeWithCoins> getExchanges() {
        return exchangeRegistry.getRegisteredExchanges().stream()
                .map(ex -> new ExchangeWithCoins(ex, exchangeRegistry.getCoins(ex)))
                .toList();
    }
}
