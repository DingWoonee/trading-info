package tradinginfo.leadingcoin.repository.dto;

import tradinginfo.leadingcoin.model.Exchange;

public record TradeAmountDto(
        Exchange exchange,
        String market,
        double tradeAmount,
        long timestamp
) {

}
