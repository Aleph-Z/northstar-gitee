package tech.quantit.northstar;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
import tech.quantit.northstar.common.IContractManager;
import tech.quantit.northstar.common.constant.GatewayType;
import tech.quantit.northstar.common.model.ContractDefinition;
import tech.quantit.northstar.gateway.api.ContractProviderComponent;
import tech.quantit.northstar.gateway.api.ICategorizedContractProvider;
import xyz.redtorch.pb.CoreEnum.ProductClassEnum;
import xyz.redtorch.pb.CoreField.ContractField;

@Slf4j
@ContractProviderComponent(GatewayType.CTP)
public class CtpFutureContractProvider implements ICategorizedContractProvider, InitializingBean{

	@Autowired
	IContractManager contractMgr;
	
	@Override
	public String nameOfCategory() {
		return "CTP期货";
	}

	@Override
	public List<ContractDefinition> loadContractDefinition() {
		return contractMgr.getAllContractDefinitions()
				.stream()
				.filter(item -> item.getGatewayType() == GatewayType.CTP && item.getProductClass() == ProductClassEnum.FUTURES)
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

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("加载CtpFutureContractProvider");
	}

}
