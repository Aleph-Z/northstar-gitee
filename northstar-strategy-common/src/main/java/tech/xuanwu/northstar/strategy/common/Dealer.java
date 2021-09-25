package tech.xuanwu.northstar.strategy.common;

import java.util.Optional;
import java.util.Set;

import tech.xuanwu.northstar.common.model.ContractManager;
import xyz.redtorch.pb.CoreEnum.OffsetFlagEnum;
import xyz.redtorch.pb.CoreField.SubmitOrderReqField;
import xyz.redtorch.pb.CoreField.TickField;
import xyz.redtorch.pb.CoreField.TradeField;

/**
 * 交易策略负责交易管理，例如接收信号、监听止损触发、及实现其他下单逻辑
 * @author KevinHuangwl
 *
 */
public interface Dealer extends DynamicParamsAware {
	
	/**
	 * 监听行情变动,生成相应的委托单
	 * @param tick
	 * @param riskRules
	 * @param gateway
	 */
	Optional<SubmitOrderReqField> onTick(TickField tick);
	
	/**
	 * 收到信号
	 * @param signal
	 * @param offsetFlag	实操明细
	 */
	void onSignal(Signal signal, OffsetFlagEnum offsetFlag);
	
	/**
	 * 收到交易回报
	 * @param trade
	 */
	void onTrade(TradeField trade);
	
	/**
	 * 获取交易策略所绑定的合约列表
	 * @return
	 */
	Set<String> bindedUnifiedSymbols();
	
	/**
	 * 设置合约管理器
	 * @param contractMgr
	 */
	void setContractManager(ContractManager contractMgr);
}
