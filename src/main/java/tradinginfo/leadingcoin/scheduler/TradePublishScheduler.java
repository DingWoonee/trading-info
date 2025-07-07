package tradinginfo.leadingcoin.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tradinginfo.leadingcoin.service.TradePublishService;

@Component
@RequiredArgsConstructor
public class TradePublishScheduler {

    private final TradePublishService tradePublishService;

    @Scheduled(fixedRate = 3000)
    public void schedule() {
        tradePublishService.publish();
    }
}
