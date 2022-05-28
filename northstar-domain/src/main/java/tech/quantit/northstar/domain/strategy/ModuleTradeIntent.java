package tech.quantit.northstar.domain.strategy;

import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import tech.quantit.northstar.common.constant.Constants;
import tech.quantit.northstar.common.utils.FieldUtils;
import tech.quantit.northstar.strategy.api.model.ModuleDealRecord;
import xyz.redtorch.pb.CoreEnum.OrderStatusEnum;
import xyz.redtorch.pb.CoreField.OrderField;
import xyz.redtorch.pb.CoreField.SubmitOrderReqField;
import xyz.redtorch.pb.CoreField.TradeField;

/**
 * 模组交易意图，用于管理下单的整个过程
 * @author KevinHuangwl
 *
 */
public class ModuleTradeIntent {
	@Getter
	private SubmitOrderReqField submitOrderReq;
	
	private ModulePosition currentPosition;
	// 平仓回调
	private Consumer<ModuleDealRecord> closeCallback; 
	// 意图结束回调
	private Runnable doneCallback;
	
	private String moduleName;
	
	public ModuleTradeIntent(String moduleName, SubmitOrderReqField submitOrderReq, Consumer<ModuleDealRecord> closeCallback, Runnable doneCallback) {
		this.submitOrderReq = submitOrderReq;
		this.closeCallback = closeCallback;
		this.moduleName = moduleName;
		this.doneCallback = doneCallback;
	}
	
	public ModuleTradeIntent(String moduleName, ModulePosition position, SubmitOrderReqField submitOrderReq,
			Consumer<ModuleDealRecord> closeCallback, Runnable doneCallback) {
		this.submitOrderReq = submitOrderReq;
		this.currentPosition = position;
		this.moduleName = moduleName;
		this.closeCallback = closeCallback;
		this.doneCallback = doneCallback;
	}
	
	public String originOrderId() {
		return submitOrderReq.getOriginOrderId();
	}
	
	// Order反馈可能是多次
	// 情况一：一次性成交
	// 情况二：分多次成交
	// 情况三：未成交就撤单
	// 情况四：部分成交然后撤单
	// 情况五：废单
	public void onOrder(OrderField order) {
		// 校验是匹配的回报
		if(!StringUtils.equals(order.getOriginOrderId(), submitOrderReq.getOriginOrderId())) 
			return;
		
		// 处理情况三、四、五
		if(order.getOrderStatus() == OrderStatusEnum.OS_Canceled || order.getOrderStatus() == OrderStatusEnum.OS_Rejected) {
			doneCallback.run();
		}
	}
	
	// Trade反馈可能是多次
	public void onTrade(TradeField trade) {
		// 校验是匹配的回报
		if(!StringUtils.equals(trade.getOriginOrderId(), submitOrderReq.getOriginOrderId())) 
			return;
		
		// 处理情况一、二、四
		if(FieldUtils.isClose(trade.getOffsetFlag())) {
			closeCallback.accept(genDealRecord(trade));
		}
		if(trade.getVolume() == submitOrderReq.getVolume()) {
			doneCallback.run();
		}
		
	}
	
	public int volume() {
		return submitOrderReq.getVolume();
	}
	
	private ModuleDealRecord genDealRecord(TradeField latestTrade) {
		double openPrice = currentPosition.getOpenPrice();
		double multiplier = latestTrade.getContract().getMultiplier();
		int factor = FieldUtils.isLong(currentPosition.getDirection()) ? 1 : -1;
		double closeProfit = factor * (latestTrade.getPrice() - openPrice) * latestTrade.getVolume() * multiplier;
		double occupiedMoney = Math.max(openPrice, latestTrade.getPrice()) * latestTrade.getVolume() * multiplier
				* (factor > 0 ? latestTrade.getContract().getLongMarginRatio() : latestTrade.getContract().getShortMarginRatio()) * Constants.ESTIMATED_FROZEN_FACTOR;
		return ModuleDealRecord.builder()
				.moduleName(moduleName)
				.contractName(latestTrade.getContract().getFullName())
				.direction(currentPosition.getDirection())
				.tradingDay(latestTrade.getTradingDay())
				.closeTimestamp(latestTrade.getTradeTimestamp())
				.openTimestamp(currentPosition.getOpenTime())
				.volume(latestTrade.getVolume())
				.openPrice(openPrice)
				.closePrice(latestTrade.getPrice())
				.closeProfit((int) closeProfit)
				.estimatedOccupiedMoney(occupiedMoney)
				.build();
	}
}