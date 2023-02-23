package tech.quantit.northstar.gateway.api.w3;

import tech.quantit.northstar.common.constant.ChannelType;
import tech.quantit.northstar.gateway.api.MarketGateway;
import w3.exchange.pb.W3CoreField.ContractField;

public interface W3MarketGateway extends MarketGateway {

	/**
	 * 订阅
	 * @param contract
	 */
	boolean subscribe(ContractField contract);

	/**
	 * 退订
	 * @param contract
	 */
	boolean unsubscribe(ContractField contract);
	
	/**
	 * 检测是否有行情数据
	 * @return
	 */
	boolean isActive();
	
	/**
	 * 网关类型
	 * @return
	 */
	ChannelType channelType();
}
