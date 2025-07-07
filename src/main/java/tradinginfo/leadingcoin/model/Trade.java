package tradinginfo.leadingcoin.model;

import java.time.Instant;

public record Trade(
        Exchange exchange,
        String market,
        double tradeAmount,
        Instant timestamp
) {

    public CoinKey key() {
        return new CoinKey(exchange, market);
    }
}
