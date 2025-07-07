package tradinginfo.leadingcoin.webclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpbitCoin(
        @JsonProperty("market") String market,
        @JsonProperty("korean_name") String koreanName,
        @JsonProperty("english_name") String englishName,
        @JsonProperty("market_event") MarketEvent marketEvent
) {

    public record MarketEvent(
            boolean warning,
            Caution caution
    ) {

        public record Caution(
                @JsonProperty("PRICE_FLUCTUATIONS") boolean priceFluctuations,
                @JsonProperty("TRADING_VOLUME_SOARING") boolean tradingVolumeSoaring,
                @JsonProperty("DEPOSIT_AMOUNT_SOARING") boolean depositAmountSoaring,
                @JsonProperty("GLOBAL_PRICE_DIFFERENCES") boolean globalPriceDifferences,
                @JsonProperty("CONCENTRATION_OF_SMALL_ACCOUNTS") boolean concentrationOfSmallAccounts
        ) {

        }
    }

}
