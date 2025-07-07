package tradinginfo.leadingcoin.websocket.processor;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import tradinginfo.leadingcoin.model.Exchange;
import tradinginfo.leadingcoin.model.Trade;
import tradinginfo.leadingcoin.repository.TradeRepository;
import tradinginfo.leadingcoin.utils.JsonUtils;
import tradinginfo.leadingcoin.websocket.dto.UpbitTrade;

@RequiredArgsConstructor
public class UpbitTradeProcessor implements TradeProcessor {

    private final TradeRepository tradeRepository;

    @Override
    public void process(String json) {
        UpbitTrade trade = JsonUtils.stringToObject(json, UpbitTrade.class);
        double amount = trade.volume() * trade.price();

        tradeRepository.save(
                new Trade(Exchange.UPBIT, trade.market(), amount, Instant.ofEpochMilli(trade.ts()))
        );

    }
}
