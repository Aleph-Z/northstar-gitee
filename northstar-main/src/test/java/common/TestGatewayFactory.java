package common;

import java.util.List;

import tech.quantit.northstar.common.constant.GatewayUsage;
import tech.quantit.northstar.common.model.CtpSettings;
import tech.quantit.northstar.common.model.GatewayDescription;
import tech.quantit.northstar.common.model.GatewaySettings;

public class TestGatewayFactory {

	public static GatewaySettings makeGatewaySettings(Class<? extends GatewaySettings> type) {
		if(type == CtpSettings.class) {
			return CtpSettings.builder()
					.brokerId("9999")
					.userId("kevin")
					.password("123456")
					.build();
		}
		return null;
	}
	
	public static GatewayDescription makeMktGateway(String id, String type, Object settings, boolean autoConnect) {
		return GatewayDescription.builder()
				.gatewayId(id)
				.autoConnect(autoConnect)
				.gatewayType(type)
				.settings(settings)
				.gatewayUsage(GatewayUsage.MARKET_DATA)
				.subscribedContractGroups(List.of("CONTRACT_GROUP1", "CONTRACT_GROUP2"))
				.build();
	}
	
	public static GatewayDescription makeTrdGateway(String id, String bindGateway, String type, Object settings, boolean autoConnect) {
		return GatewayDescription.builder()
				.gatewayId(id)
				.autoConnect(autoConnect)
				.gatewayType(type)
				.settings(settings)
				.gatewayUsage(GatewayUsage.TRADE)
				.bindedMktGatewayId(bindGateway)
				.build();
	}
}
