package tech.quantit.northstar.gateway.okx.time;

import tech.quantit.northstar.gateway.api.domain.time.PeriodSegment;
import tech.quantit.northstar.gateway.api.domain.time.TradeTimeDefinition;

import java.time.LocalTime;
import java.util.List;

/**
 * 币圈连续交易时段  全天候
 * @author
 *
 */
public class W3TokenTradeTime implements TradeTimeDefinition{

	@Override
	public List<PeriodSegment> tradeTimeSegments() {
		return List.of(
				new PeriodSegment(LocalTime.of(12, 0), LocalTime.of(24, 0))
		);
	}

}
