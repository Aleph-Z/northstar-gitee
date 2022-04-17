package tech.quantit.northstar.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 模组账户信息
 * @author KevinHuangwl
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleAccountDescription {
	
	/**
	 * 初始余额
	 */
	private double initBalance;

	/**
	 * 期初余额（开仓前计算）
	 */
	private double preBalance;
	
}