package tradinginfo.leadingcoin.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import tradinginfo.leadingcoin.global.utils.JsonUtils;

@Slf4j
@Component
@ConditionalOnProperty(name = "app.message-broker", havingValue = "stomp", matchIfMissing = true)
@RequiredArgsConstructor
public class StompMessagePublisher implements MessagePublisher {

    private final SimpMessagingTemplate template;

    @Override
    public void publish(String destination, Object payload) {
        template.convertAndSend(destination, JsonUtils.objectToString(payload));
    }
}
