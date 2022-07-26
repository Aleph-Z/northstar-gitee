package tech.quantit.northstar.main.restful;

import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.quantit.northstar.common.constant.GatewayType;
import tech.quantit.northstar.common.model.ContractDefinition;
import tech.quantit.northstar.common.model.ResultBean;
import tech.quantit.northstar.gateway.api.ContractProviderComponent;
import tech.quantit.northstar.gateway.api.ICategorizedContractProvider;

@RequestMapping("/northstar/contract")
@RestController
public class ContractController {
	
	@Autowired
	List<ICategorizedContractProvider> contractProviders;

	@GetMapping("/defs")
	@NotNull(message="网关类型不能为空")
	public ResultBean<List<ContractDefinition>> getContractDefinitions(GatewayType gatewayType){
//		List<ICategorizedContractProvider> contractProviders = List.of(contractProvider);
		return new ResultBean<>(contractProviders.stream()
				.filter(pvd -> pvd.getClass().getDeclaredAnnotation(ContractProviderComponent.class).value() == gatewayType)
				.map(ICategorizedContractProvider::loadContractDefinition)
				.flatMap(Collection::stream)
				.toList());
	} 
	
}
