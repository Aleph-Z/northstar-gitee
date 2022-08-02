package tech.quantit.northstar.gateway.sim;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.quantit.northstar.common.GatewayType;
import tech.quantit.northstar.common.constant.GatewayUsage;
import tech.quantit.northstar.gateway.api.GatewayTypeProvider;

@Component
public class SIM implements GatewayType, InitializingBean{

	@Autowired
	GatewayTypeProvider gtp;
	
	@Override
	public GatewayUsage[] usage() {
		return new GatewayUsage[] {GatewayUsage.MARKET_DATA, GatewayUsage.TRADE};
	}

	@Override
	public boolean adminOnly() {
		return false;
	}

	@Override
	public String name() {
		return getClass().getName();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		gtp.addGatewayType(this);
	}

}
