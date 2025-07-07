package tradinginfo.leadingcoin.websocket.builder;

import java.util.List;
import java.util.Map;
import tradinginfo.leadingcoin.model.Coin;

public interface PayloadBuilder {

    List<Map<String, Object>> buildPayload(List<Coin> coins);
}
