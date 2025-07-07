package tradinginfo.leadingcoin.service.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import tradinginfo.leadingcoin.model.Coin;
import tradinginfo.leadingcoin.model.Exchange;

@Builder
public record ExchangeWithCoins(
        Exchange exchange,
        List<Coin> coins
) {

}
