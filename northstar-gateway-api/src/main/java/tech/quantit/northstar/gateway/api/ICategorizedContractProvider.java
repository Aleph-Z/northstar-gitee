package tech.quantit.northstar.gateway.api;

import java.util.List;

import tech.quantit.northstar.common.model.ContractDefinition;
import xyz.redtorch.pb.CoreField.ContractField;

public interface ICategorizedContractProvider {

	String nameOfCategory();
	
	List<ContractDefinition> loadContractDefinition();
	
	List<ContractField> loadContract();
	
}
