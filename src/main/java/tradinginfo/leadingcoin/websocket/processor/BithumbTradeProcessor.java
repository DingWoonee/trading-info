package tradinginfo.leadingcoin.websocket.processor;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import tradinginfo.leadingcoin.model.Exchange;
import tradinginfo.leadingcoin.model.Trade;
import tradinginfo.leadingcoin.repository.TradeRepository;
import tradinginfo.leadingcoin.global.utils.JsonUtils;
import tradinginfo.leadingcoin.websocket.dto.BithumbTrade;

@RequiredArgsConstructor
public class BithumbTradeProcessor implements TradeProcessor {

    private final TradeRepository tradeRepository;

    @Override
    public void process(String json) {
        BithumbTrade trade = JsonUtils.stringToObject(json, BithumbTrade.class);
        double amount = trade.volume() * trade.price();

        tradeRepository.save(
                new Trade(Exchange.BITHUMB, trade.market(), amount, Instant.ofEpochMilli(trade.ts()))
        );
    }
}
