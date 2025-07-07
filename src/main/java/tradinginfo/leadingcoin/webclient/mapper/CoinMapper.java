package tradinginfo.leadingcoin.webclient.mapper;

import tradinginfo.leadingcoin.model.Coin;

public interface CoinMapper<D> {

    Coin toCoin(D dto);
}
