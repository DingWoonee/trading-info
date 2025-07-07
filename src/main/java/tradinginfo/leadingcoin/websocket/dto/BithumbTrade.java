package tradinginfo.leadingcoin.websocket.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BithumbTrade(
        @JsonProperty("trade_price") double price,
        @JsonProperty("trade_volume") double volume,
        @JsonProperty("code") String market,
        @JsonProperty("trade_timestamp") long ts
) {
}
