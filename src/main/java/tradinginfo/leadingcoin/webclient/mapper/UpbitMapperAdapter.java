package tradinginfo.leadingcoin.webclient.mapper;

import tradinginfo.leadingcoin.model.Coin;
import tradinginfo.leadingcoin.webclient.dto.UpbitCoin;
import tradinginfo.leadingcoin.webclient.dto.UpbitCoin.MarketEvent;

public class UpbitMapperAdapter implements CoinMapper<UpbitCoin> {

    @Override
    public Coin toCoin(UpbitCoin dto) {
        return Coin.builder()
                .market(dto.market())
                .krName(dto.koreanName())
                .enName(dto.englishName())
                .isWarning(isWarning(dto.marketEvent()))
                .build();
    }

    private boolean isWarning(MarketEvent marketEvent) {
        if (marketEvent == null) {
            return false;
        }
        if (marketEvent.warning()) {
            return true;
        }
        if (marketEvent.caution() == null) {
            return false;
        }
        return marketEvent.caution().priceFluctuations()
                || marketEvent.caution().tradingVolumeSoaring()
                || marketEvent.caution().depositAmountSoaring()
                || marketEvent.caution().globalPriceDifferences()
                || marketEvent.caution().concentrationOfSmallAccounts();
    }
}
