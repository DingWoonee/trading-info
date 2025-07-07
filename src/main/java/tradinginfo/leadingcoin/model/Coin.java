package tradinginfo.leadingcoin.model;

import lombok.Builder;

@Builder
public record Coin(
        Exchange exchange,
        String market,
        String krName,
        String enName,
        boolean isWarning
) {

    public CoinKey key() {
        return new CoinKey(exchange, market);
    }
}
