package tradinginfo.leadingcoin.websocket.builder;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import tradinginfo.leadingcoin.model.Coin;

public class BithumbPayloadBuilder implements PayloadBuilder {

    @Override
    public List<Map<String, Object>> buildPayload(List<Coin> coins) {
        return List.of(
                Map.of("ticket", UUID.randomUUID().toString()),
                Map.of("type", "trade",
                        "codes", coins.stream().map(Coin::market).toList(),
                        "isOnlyRealtime", true),
                Map.of("format", "DEFAULT")
        );
    }
}
