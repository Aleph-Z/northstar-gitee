package tech.xuanwu.northstar.strategy.common.model.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.redtorch.pb.CoreEnum.PositionDirectionEnum;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ModulePositionPO {

	private PositionDirectionEnum positionDir;
	
	private double openPrice;
	
	private double stopLossPrice;
	
	private String unifiedSymbol;
	
	private int volume;
	
	private double multiplier;
	
}
