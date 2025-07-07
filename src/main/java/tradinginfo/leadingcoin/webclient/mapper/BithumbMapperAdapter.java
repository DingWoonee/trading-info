package tradinginfo.leadingcoin.webclient.mapper;

import tradinginfo.leadingcoin.model.Coin;
import tradinginfo.leadingcoin.webclient.dto.BithumbCoin;
import tradinginfo.leadingcoin.webclient.dto.BithumbCoin.BithumbWarning;

public class BithumbMapperAdapter implements CoinMapper<BithumbCoin> {

    @Override
    public Coin toCoin(BithumbCoin dto) {
        return Coin.builder()
                .market(dto.market())
                .krName(dto.koreanName())
                .enName(dto.englishName())
                .isWarning(isWarning(dto.marketWarning()))
                .build();
    }

    private boolean isWarning(BithumbWarning bithumbWarning) {
        return bithumbWarning == BithumbWarning.CAUTION;
    }
}
