package tradinginfo.leadingcoin.webclient.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ApiClient {

    private final WebClient webClient;

    public ApiClient(WebClient.Builder builder) {
        this.webClient = builder
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public <T> Mono<T> get(String url, ParameterizedTypeReference<T> typeRef) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(typeRef);
    }
}
