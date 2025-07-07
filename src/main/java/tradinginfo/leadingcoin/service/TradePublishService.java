package tradinginfo.leadingcoin.service;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tradinginfo.leadingcoin.messaging.MessagePublisher;
import tradinginfo.leadingcoin.model.Exchange;
import tradinginfo.leadingcoin.repository.TradeRepository;
import tradinginfo.leadingcoin.repository.dto.TradeAmountDto;

@Service
@RequiredArgsConstructor
public class TradePublishService {

    private final MessagePublisher messagePublisher;
    private final TradeRepository tradeRepository;

    public void publish() {
        Map<Exchange, List<TradeAmountDto>> data = tradeRepository.getTop10TradeAmounts();
        data.keySet().forEach(exchange ->
                messagePublisher.publish(
                        String.format("/topic/top10/%s", exchange.name()),
                        data.get(exchange)
                )
        );
    }
}
