package tradinginfo.leadingcoin.websocket.client;

import java.net.URI;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import tradinginfo.leadingcoin.websocket.processor.TradeProcessor;

@Slf4j
@Component
@RequiredArgsConstructor
public class WsClient {

    private final org.springframework.web.reactive.socket.client.WebSocketClient wsClient = new ReactorNettyWebSocketClient();

    public void connectWs(String wsUrl, String payload, TradeProcessor tradeProcessor) {
        wsClient.execute(URI.create(wsUrl), session ->
                        session.send(Mono.just(session.textMessage(payload)))
                                .thenMany(
                                        session.receive()
                                                .map(WebSocketMessage::getPayloadAsText)
                                                .doOnNext(tradeProcessor::process)
                                                .onBackpressureDrop()
                                )
                                .then()
                                .doOnSuccess(v -> log.info("WebSocket session closed normally: {}", wsUrl))
                                .doOnError(err -> log.warn("WebSocket session error during data flow: {}", wsUrl, err))
                )
                .retryWhen(Retry.backoff(Long.MAX_VALUE, Duration.ofSeconds(5))
                        .maxBackoff(Duration.ofMinutes(1))
                        .jitter(0.5))
                .doOnError(err -> log.error("WS 치명 오류", err))
                .subscribe();
    }
}
