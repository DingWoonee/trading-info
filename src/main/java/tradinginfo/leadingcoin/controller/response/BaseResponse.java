package tradinginfo.leadingcoin.controller.response;

public record BaseResponse<T>(
        boolean isSuccess,
        String message,
        T data
) {

}
