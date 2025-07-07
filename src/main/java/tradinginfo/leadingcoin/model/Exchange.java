package tradinginfo.leadingcoin.model;

import lombok.Getter;

@Getter
public enum Exchange {

    UPBIT(
            "https://api.upbit.com/v1/market/all?is_details=true",
            "wss://api.upbit.com/websocket/v1",
            "/img/upbit.png"
    ),
    BITHUMB(
            "https://api.bithumb.com/v1/market/all?isDetails=true",
            "wss://ws-api.bithumb.com/websocket/v1",
            "/img/bithumb.png"
    );

    private final String apiUrl;
    private final String wsUrl;
    private final String imgUri;

    Exchange(String apiUrl, String wsUrl, String imgUri) {
        this.apiUrl = apiUrl;
        this.wsUrl = wsUrl;
        this.imgUri = imgUri;
    }
}
