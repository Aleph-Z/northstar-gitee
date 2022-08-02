package tech.quantit.northstar.gateway.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import tech.quantit.northstar.common.GatewayType;

@Component
public class GatewayTypeProvider {

	Map<String, GatewayType> typeMap = new HashMap<>();
	
	public void addGatewayType(GatewayType gatewayType) {
		typeMap.put(gatewayType.name(), gatewayType);
	}
	
	public GatewayType valueOf(String name) {
		if(!typeMap.containsKey(name)) {
			throw new IllegalStateException("不存在该网关类型：" + name);
		}
		return typeMap.get(name);
	}
	
	
}
