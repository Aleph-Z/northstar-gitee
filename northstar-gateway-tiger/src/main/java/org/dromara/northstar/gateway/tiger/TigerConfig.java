package org.dromara.northstar.gateway.tiger;

import org.dromara.northstar.common.event.FastEventEngine;
import org.dromara.northstar.gateway.IMarketCenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class TigerConfig {
	
	static {
		log.info("=====================================================");
		log.info("                  加载gateway-tiger                   ");
		log.info("=====================================================");
	}
	
	@Bean
	TigerGatewayFactory tigerGatewayFactory(FastEventEngine feEngine, IMarketCenter marketCenter) {
		return new TigerGatewayFactory(feEngine, marketCenter);
	}
	
	@Bean
	TigerDataServiceManager tigerDataServiceManager() {
		return new TigerDataServiceManager();
	}
}
