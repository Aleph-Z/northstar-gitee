package tech.quantit.northstar.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 交易开平仓记录
 * @author KevinHuangwl
 *
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ModuleDealRecord {
	/**
	 * 模组名称
	 */
	private String moduleName;
	/**
	 * 模组账户
	 */
	private String moduleAccountId;
	/**
	 * 合约中文名称
	 */
	private String contractName;
	/**
	 * 平仓盈亏
	 */
	private double dealProfit;
	/**
	 * 开仓成交
	 */
	private byte[] openTrade;
	/**
	 * 平仓成交
	 */
	private byte[] closeTrade;

	/**
	 * 创建时间
	 */
//	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss[.SSS]")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;
}
