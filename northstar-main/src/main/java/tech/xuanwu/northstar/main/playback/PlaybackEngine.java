package tech.xuanwu.northstar.main.playback;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import tech.xuanwu.northstar.common.constant.Constants;
import tech.xuanwu.northstar.gateway.sim.trade.SimTradeGateway;
import tech.xuanwu.northstar.main.persistence.po.MinBarDataPO;
import tech.xuanwu.northstar.main.persistence.po.TickDataPO;
import xyz.redtorch.pb.CoreField.BarField;
import xyz.redtorch.pb.CoreField.TickField;

/**
 * 回测引擎负责把原始的历史行情数据按时间规则重放来模拟真实行情
 * @author KevinHuangwl
 *
 */
public class PlaybackEngine {

	public void play(PlaybackTask task, SimTradeGateway gateway) {
		while(!task.isDone()) {
			Map<String, Iterator<MinBarDataPO>> symbolBatchDataMap = task.nextBatchData();
			PriorityQueue<TickField> tickQ = new PriorityQueue<>(100000, (t1, t2) -> t1.getActionTimestamp() < t2.getActionTimestamp() ? -1 : 1 );
			
			PriorityQueue<BarField> barQ = new PriorityQueue<>(3000, (b1, b2) -> b1.getActionTimestamp() < b2.getActionTimestamp() ? -1 : 1 );
			
			// 先把三维的TICK数据转成一维
			for(Entry<String, Iterator<MinBarDataPO>> e : symbolBatchDataMap.entrySet()) {
				Iterator<MinBarDataPO> itDailyData = e.getValue();
				while(itDailyData.hasNext()) {
					MinBarDataPO minBar = itDailyData.next();
					for(TickDataPO tickData : minBar.getTicksOfMin()) {
						tickQ.offer(restorePlaybackTick(minBar, tickData));
					}
					barQ.offer(restorePlaybackBar(minBar));
				}
			}
			
			while(!barQ.isEmpty()) {
				BarField bar = barQ.poll();
				while(!tickQ.isEmpty() && tickQ.peek().getActionTimestamp() < bar.getActionTimestamp() + 60000) {					
					TickField tick = tickQ.poll();
					gateway.onTick(tick);
					task.getPlaybackModules().forEach(module -> module.onTick(tick));
				}
				task.getPlaybackModules().forEach(module -> module.onBar(bar));
			}
		}
	}
	
	private BarField restorePlaybackBar(MinBarDataPO barData) {
		return BarField.newBuilder()
				.setActionDay(barData.getActionDay())
				.setActionTime(barData.getActionTime())
				.setActionTimestamp(barData.getActionTimestamp())
				.setTradingDay(barData.getTradingDay())
				.setGatewayId(Constants.PLAYBACK_GATEWAY)
				.setUnifiedSymbol(barData.getUnifiedSymbol())
				.setHighPrice(barData.getHighPrice())
				.setLowPrice(barData.getLowPrice())
				.setOpenPrice(barData.getOpenPrice())
				.setClosePrice(barData.getClosePrice())
				.setPreClosePrice(barData.getPreClosePrice())
				.setPreOpenInterest(barData.getPreOpenInterest())
				.setPreSettlePrice(barData.getPreSettlePrice())
				.setVolume(barData.getVolume())
				.setVolumeDelta(barData.getVolumeDelta())
				.setOpenInterest(barData.getOpenInterest())
				.setOpenInterestDelta(barData.getOpenInterestDelta())
				.setNumTrades(barData.getNumTrades())
				.setNumTradesDelta(barData.getNumTradesDelta())
				.setTurnover(barData.getTurnover())
				.setTurnoverDelta(barData.getTurnoverDelta())
				.build();
	}
	
	private TickField restorePlaybackTick(MinBarDataPO barData, TickDataPO tickData) {
		return TickField.newBuilder()
				.setActionDay(barData.getActionDay())
				.setActionTime(tickData.getActionTime())
				.setActionTimestamp(tickData.getActionTimestamp())
				.setTradingDay(barData.getTradingDay())
				.addAskPrice(tickData.getAskPrice1())
				.addBidPrice(tickData.getBidPrice1())
				.setGatewayId(Constants.PLAYBACK_GATEWAY)
				.setLastPrice(tickData.getLastPrice())
				.setAvgPrice(tickData.getAvgPrice())
				.setUnifiedSymbol(barData.getUnifiedSymbol())
				.setTurnover(tickData.getTurnover())
				.setTurnoverDelta(tickData.getTurnoverDelta())
				.addAskVolume(tickData.getAskVol1())
				.addBidVolume(tickData.getBidVol1())
				.setVolume(tickData.getVolume())
				.setVolumeDelta(tickData.getVolumeDelta())
				.setNumTrades(tickData.getNumTrades())
				.setNumTradesDelta(tickData.getNumTradesDelta())
				.setOpenInterest(tickData.getOpenInterest())
				.setOpenInterestDelta(tickData.getOpenInterestDelta())
				.setPreClosePrice(barData.getPreClosePrice())
				.setPreOpenInterest(barData.getPreOpenInterest())
				.setPreSettlePrice(barData.getPreSettlePrice())
				.build();
	}
	
}
