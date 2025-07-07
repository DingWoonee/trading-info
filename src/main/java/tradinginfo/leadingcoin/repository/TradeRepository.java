package tradinginfo.leadingcoin.repository;

import java.util.List;
import java.util.Map;
import tradinginfo.leadingcoin.model.Exchange;
import tradinginfo.leadingcoin.model.Trade;
import tradinginfo.leadingcoin.repository.dto.TradeAmountDto;

public interface TradeRepository {

    void save(Trade trade);

    List<TradeAmountDto> getTradeAmounts();

    Map<Exchange, List<TradeAmountDto>> getTop10TradeAmounts();
}
