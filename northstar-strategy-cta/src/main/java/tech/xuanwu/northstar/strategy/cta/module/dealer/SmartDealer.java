package tech.xuanwu.northstar.strategy.cta.module.dealer;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import tech.xuanwu.northstar.strategy.common.Dealer;
import tech.xuanwu.northstar.strategy.common.annotation.Setting;
import tech.xuanwu.northstar.strategy.common.annotation.StrategicComponent;
import tech.xuanwu.northstar.strategy.common.model.meta.DynamicParams;
import xyz.redtorch.pb.CoreField.SubmitOrderReqField;
import xyz.redtorch.pb.CoreField.TickField;
import xyz.redtorch.pb.CoreField.TradeField;

@Slf4j
@StrategicComponent("智盈交易策略")
public class SmartDealer extends AbstractDealer implements Dealer {

	
	@Override
	public Optional<SubmitOrderReqField> onTick(TickField tick) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onTrade(TradeField trade) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public DynamicParams getDynamicParams() {
		return new InitParams();
	}

	@Override
	public void initWithParams(DynamicParams params) {
		InitParams initParams = (InitParams) params;
		this.bindedUnifiedSymbol = initParams.bindedUnifiedSymbol;
		this.openVol = initParams.openVol;
		this.priceTypeStr = initParams.priceTypeStr;
		this.overprice = initParams.overprice;
	}

	public static class InitParams extends DynamicParams{

		@Setting(value="绑定合约", order = 10)
		private String bindedUnifiedSymbol;
		
		@Setting(value="开仓手数", order = 20)
		private int openVol;
		
		@Setting(value="价格类型", order = 30, options = {"对手价", "市价", "最新价", "排队价", "信号价"})
		private String priceTypeStr;
		
		@Setting(value="超价", order = 40, unit = "Tick")
		private int overprice;
	}
	
}
