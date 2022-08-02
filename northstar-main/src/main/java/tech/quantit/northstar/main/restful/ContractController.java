package tech.quantit.northstar.main.restful;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.quantit.northstar.common.model.ContractDefinition;
import tech.quantit.northstar.common.model.ResultBean;
import tech.quantit.northstar.gateway.api.ICategorizedContractProvider;
import xyz.redtorch.pb.CoreField.ContractField;

@RequestMapping("/northstar/contract")
@RestController
public class ContractController {
	
	@Autowired
	List<ICategorizedContractProvider> contractProviders;
	
	final CacheControl cacheControl = CacheControl
			.maxAge(1, TimeUnit.DAYS)
		    .noTransform()
		    .mustRevalidate();

	@GetMapping("/defs")
	@NotNull(message="合约类别名称不能为空")
	public ResponseEntity<ResultBean<List<ContractDefinition>>> getContractDefinitions(String name){
		ResultBean<List<ContractDefinition>> body =new ResultBean<>(contractProviders.stream()
				.filter(pvd -> pvd.nameOfCategory().equals(name))
				.map(ICategorizedContractProvider::loadContractDefinitions)
				.flatMap(Collection::stream)
				.toList());
		return ResponseEntity.ok()
			      .cacheControl(cacheControl)
			      .body(body);
	}
	
	@GetMapping("/list")
	@NotNull(message="合约类别名称不能为空")
	public ResponseEntity<ResultBean<List<byte[]>>> getContractList(String name){
		ResultBean<List<byte[]>> body = new ResultBean<>(contractProviders.stream()
				.filter(pvd -> pvd.nameOfCategory().equals(name))
				.map(ICategorizedContractProvider::loadContracts)
				.flatMap(Collection::stream)
				.map(ContractField::toByteArray)
				.toList());
		return ResponseEntity.ok()
			      .cacheControl(cacheControl)
			      .body(body);
	}

	@GetMapping("/providers")
	@NotNull(message="网关类型不能为空")
	public ResponseEntity<ResultBean<List<String>>> providerList(String gatewayType){
		ResultBean<List<String>> body = new ResultBean<>(contractProviders.stream()
				.filter(pvd -> pvd.gatewayType().name().equals(gatewayType))
				.map(ICategorizedContractProvider::nameOfCategory)
				.toList());
		return ResponseEntity.ok()
			      .cacheControl(cacheControl)
			      .body(body);
	}
}
