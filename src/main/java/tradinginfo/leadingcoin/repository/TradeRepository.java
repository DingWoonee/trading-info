package tradinginfo.leadingcoin.repository;

import java.util.List;
import tradinginfo.leadingcoin.model.Trade;
import tradinginfo.leadingcoin.repository.dto.TradeAmountDto;

public interface TradeRepository {

    void save(Trade trade);

    List<TradeAmountDto> getTradeAmounts();
}
