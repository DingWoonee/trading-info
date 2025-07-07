package tradinginfo.leadingcoin.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tradinginfo.leadingcoin.controller.response.BaseResponse;
import tradinginfo.leadingcoin.controller.response.ExchangeWithCoinsResponse;
import tradinginfo.leadingcoin.service.ExchangeService;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final ExchangeService exchangeService;

    @GetMapping("/api/coins")
    public BaseResponse<List<ExchangeWithCoinsResponse>> getCoins() {
        List<ExchangeWithCoinsResponse> data = exchangeService.getExchangesAndCoins().stream()
                .map(ExchangeWithCoinsResponse::from)
                .toList();

        return new BaseResponse<>(true, null, data);
    }

    @GetMapping("/api/exchanges")
    public BaseResponse<List<String>> getExchanges() {
        List<String> data = exchangeService.getExchanges().stream()
                .map(Enum::name)
                .toList();

        return new BaseResponse<>(true, null, data);
    }
}
