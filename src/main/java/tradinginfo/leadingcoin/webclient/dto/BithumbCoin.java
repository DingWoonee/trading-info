package tradinginfo.leadingcoin.webclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BithumbCoin(
        @JsonProperty("market") String market,
        @JsonProperty("korean_name") String koreanName,
        @JsonProperty("english_name") String englishName,
        @JsonProperty("market_warning") BithumbWarning marketWarning
) {

    public enum BithumbWarning {
        NONE, CAUTION
    }
}
