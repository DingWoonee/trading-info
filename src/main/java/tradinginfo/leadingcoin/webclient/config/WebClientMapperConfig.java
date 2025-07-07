package tradinginfo.leadingcoin.webclient.config;

import java.util.EnumMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tradinginfo.leadingcoin.model.Exchange;
import tradinginfo.leadingcoin.webclient.mapper.BithumbMapperAdapter;
import tradinginfo.leadingcoin.webclient.mapper.CoinMapper;
import tradinginfo.leadingcoin.webclient.mapper.UpbitMapperAdapter;

@Configuration
public class WebClientMapperConfig {

    @Bean
    public Map<Exchange, CoinMapper<?>> webClientCoinMappers() {
        EnumMap<Exchange, CoinMapper<?>> map = new EnumMap<>(Exchange.class);
        map.put(Exchange.UPBIT, upbitMapper());
        map.put(Exchange.BITHUMB, bithumbMapper());
        return map;
    }

    @Bean
    public CoinMapper<?> upbitMapper() {
        return new UpbitMapperAdapter();
    }

    @Bean
    public CoinMapper<?> bithumbMapper() {
        return new BithumbMapperAdapter();
    }
}
