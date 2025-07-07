package tradinginfo.leadingcoin.controller.response;

import java.util.List;
import lombok.Builder;
import tradinginfo.leadingcoin.model.Coin;
import tradinginfo.leadingcoin.service.dto.ExchangeWithCoins;

@Builder
public record ExchangeWithCoinsResponse(
        String name,
        String imgUrl,
        List<CoinResponse> coins
) {

    public static ExchangeWithCoinsResponse from(ExchangeWithCoins ex) {
        return ExchangeWithCoinsResponse.builder()
                .name(ex.exchange().name())
                .imgUrl(ex.exchange().getImgUri())
                .coins(ex.coins().stream()
                        .map(CoinResponse::from)
                        .toList())
                .build();
    }

    @Builder
    public record CoinResponse(
            String market,
            String krName,
            String enName,
            boolean isWarning
    ) {

        public static CoinResponse from(Coin c) {
            return CoinResponse.builder()
                    .market(c.market())
                    .krName(c.krName())
                    .enName(c.enName())
                    .isWarning(c.isWarning())
                    .build();
        }
    }
}