package tradinginfo.leadingcoin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tradinginfo.leadingcoin.messaging.MessagePublisher;
import tradinginfo.leadingcoin.repository.TradeRepository;

@Service
@RequiredArgsConstructor
public class TradePublishService {

    private final MessagePublisher messagePublisher;
    private final TradeRepository tradeRepository;

    public void publish() {
        tradeRepository.getTradeAmounts().forEach(data ->
                messagePublisher.publish(
                        String.format("/topic/trade.%s.%s", data.exchange().name(), data.market()),
                        data
                )
        );
    }
}
