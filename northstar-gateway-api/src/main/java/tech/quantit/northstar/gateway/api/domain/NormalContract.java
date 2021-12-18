package tech.quantit.northstar.gateway.api.domain;

import lombok.NoArgsConstructor;
import tech.quantit.northstar.common.constant.GatewayType;
import xyz.redtorch.pb.CoreEnum.ProductClassEnum;
import xyz.redtorch.pb.CoreField.ContractField;

/**
 * 
 * @author KevinHuangwl
 *
 */
@NoArgsConstructor
public class NormalContract {

	protected ContractField field;
	protected GatewayType gatewayType;

	public NormalContract(ContractField field, GatewayType gatewayType) {
		this.field = field;
		this.gatewayType = gatewayType;
	}
	
	public GatewayType gatewayType() {
		return gatewayType;
	}

	public String unifiedSymbol() {
		return field.getUnifiedSymbol();
	}
	
	public ProductClassEnum productClass() {
		return field.getProductClass();
	}

	public ContractField contractField() {
		return field;
	}

	public BarGenerator barGenerator() {
		return new BarGenerator(this);
	}

}
