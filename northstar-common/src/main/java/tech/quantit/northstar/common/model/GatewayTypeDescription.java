package tech.quantit.northstar.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.quantit.northstar.common.constant.GatewayType;
import tech.quantit.northstar.common.constant.GatewayUsage;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GatewayTypeDescription {

	private GatewayType type;
	
	public GatewayUsage[] getUsage() {
		return type.getUsage();
	}
	
	public boolean isAdminOnly() {
		return type.isAdminOnly();
	}
}
