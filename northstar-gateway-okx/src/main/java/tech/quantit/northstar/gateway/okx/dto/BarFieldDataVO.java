package tech.quantit.northstar.gateway.okx.dto;

import lombok.Builder;
import lombok.Data;

/**
 * K线数据结果对象
 */
@Data
@Builder
public class BarFieldDataVO {
    /**
     * 统一合约标识
     */
    private  String unifiedSymbol = "1";
    /**
     * 网关ID
     */
    private  String gatewayId = "2";
    /**
     * 交易日
     */
    private  String tradingDay = "3";
    /**
     *业务发生日
     */
    private  String actionDay = "4";
    /**
     * 时间(HHmmssSSS)
     */
    private  String actionTime = "5";
    /**
     * 时间戳
     */
    private  float actionTimestamp = 6;
    /**
     * 开盘价
     */
    private  double openPrice = 7;
    /**
     * 最高价
     */
    private  double highPrice = 8;
    /**
     * 最低价
     */
    private  double lowPrice = 9;
    /**
     * 收盘价
     */
    private  double closePrice = 10;
    /**
     * 最后持仓量
     */
    private  double openInterest = 11;
    /**
     * 持仓量（Bar）
     */
    private  double openInterestDelta = 12;
    /**
     * 最后总成交量
     */
    private  float volume = 13;  // 最后总成交量
    /**
     * 成交量（Bar）
     */
    private  float volumeDelta = 14;  // 成交量（Bar）
    /**
     * 最后成交总额
     */
    private  double turnover = 15;
    /**
     * 成交总额（Bar）
     */
    private  double turnoverDelta = 16;
    /**
     * 最新成交笔数
     */
    private  float numTrades = 17;
    /**
     * 成交笔数（Bar）
     */
    private  float numTradesDelta = 18;
    /**
     * 昨持仓
     */
    private  double preOpenInterest = 19;
    /**
     * 前收盘价
     */
    private  double preClosePrice = 20;
    /**
     * 前结算价
     */
    private  double preSettlePrice = 21;
}
