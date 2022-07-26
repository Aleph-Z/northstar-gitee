package tech.quantit.northstar;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import tech.quantit.northstar.common.IContractManager;
import tech.quantit.northstar.common.constant.GatewayType;
import tech.quantit.northstar.common.model.ContractDefinition;
import tech.quantit.northstar.gateway.api.ContractProviderComponent;
import tech.quantit.northstar.gateway.api.ICategorizedContractProvider;
import xyz.redtorch.pb.CoreEnum.ProductClassEnum;
import xyz.redtorch.pb.CoreField.ContractField;

@ContractProviderComponent(GatewayType.CTP)
public class CtpOptionContractProvider implements ICategorizedContractProvider {
	
	@Autowired
	IContractManager contractMgr;
	
	@Override
	public String nameOfCategory() {
		return "CTP期权";
	}

	@Override
	public List<ContractDefinition> loadContractDefinition() {
		return contractMgr.getAllContractDefinitions()
				.stream()
				.filter(item -> item.getGatewayType() == GatewayType.CTP && item.getProductClass() == ProductClassEnum.OPTION)
				.toList();
	}

	@Override
	public List<ContractField> loadContract() {
		return loadContractDefinition()
				.stream()
				.map(def -> contractMgr.relativeContracts(def.contractDefId()))
				.flatMap(Collection::stream)
				.toList();
	}

}
