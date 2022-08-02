package tech.quantit.northstar;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.quantit.northstar.common.GatewayType;
import tech.quantit.northstar.common.constant.GatewayUsage;
import tech.quantit.northstar.gateway.api.GatewayTypeProvider;
import xyz.redtorch.gateway.ctp.x64v6v3v15v.CtpGatewayFactory;

@Component
public class CTP implements GatewayType, InitializingBean{

	@Autowired
	private GatewayTypeProvider gtp;
	
	@Autowired
	private CtpGatewayFactory factory;
	
	@Override
	public GatewayUsage[] usage() {
		return new GatewayUsage[]{GatewayUsage.MARKET_DATA, GatewayUsage.TRADE};
	}

	@Override
	public boolean adminOnly() {
		return false;
	}

	@Override
	public String name() {
		return getClass().getSimpleName();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		gtp.addGatewayType(this, factory);
	}

}
