package tradinginfo.leadingcoin.messaging;

public interface MessagePublisher {

    void publish(String destination, Object payload);
}
