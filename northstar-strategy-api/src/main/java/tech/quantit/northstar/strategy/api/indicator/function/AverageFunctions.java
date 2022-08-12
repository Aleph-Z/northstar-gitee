package tech.quantit.northstar.strategy.api.indicator.function;

import com.google.common.util.concurrent.AtomicDouble;
import org.apache.commons.lang3.StringUtils;
import tech.quantit.northstar.common.model.TimeSeriesValue;
import tech.quantit.northstar.strategy.api.indicator.TimeSeriesUnaryOperator;
import xyz.redtorch.pb.CoreField.BarField;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.DoubleStream;
import java.util.stream.LongStream;

/**
 * 均线函数
 * 函数名称为了与业界实践保持一致，并没有僵硬地采用驼峰命名规范，而是遵循业界常用命名
 * @author KevinHuangwl
 *
 */
public interface AverageFunctions {

	/**
	 * 当日成交量加权均价（当日结算价）函数
	 * 注意：该算法与交易所的结算价存在一定误差，主要因为该算法是按K线计算，K线周期越小，误差越小
	 * @return		返回计算函数
	 */
	static Function<BarField, TimeSeriesValue> SETTLE(){
		final AtomicDouble weightPrice = new AtomicDouble();
		final AtomicInteger countOfBarsToday = new AtomicInteger();
		final AtomicLong sumVol = new AtomicLong();
		final String[] tradeDay = {""};
		return bar -> {
			if(!StringUtils.equals(tradeDay[0], bar.getTradingDay())) {
				tradeDay[0] = bar.getTradingDay();
				sumVol.set(0);
				weightPrice.set(0);
				countOfBarsToday.set(0);
			}
			countOfBarsToday.incrementAndGet();
			sumVol.addAndGet(bar.getVolumeDelta());
			double wp = (bar.getHighPrice() + bar.getLowPrice() + bar.getClosePrice() * 2) / 4;
			double factor = 1.0 * bar.getVolumeDelta() / sumVol.get();
			double value = factor * wp + (1 - factor) * weightPrice.get();
			weightPrice.set(value);
			return new TimeSeriesValue(value, bar.getActionTimestamp());
		};
	}
	
	/**
	 * N周期内的成交量加权均价函数
	 * @param n		统计范围
	 * @return		返回计算函数
	 */
	static Function<BarField, TimeSeriesValue> SETTLE(int n){
		final long[] volArr = new long[n];
		final double[] priceArr = new double[n];
		final AtomicInteger index = new AtomicInteger(0);
		return bar -> {
			int i = index.get();
			volArr[i] = bar.getVolumeDelta();
			priceArr[i] = (bar.getClosePrice() * 2 + bar.getHighPrice() + bar.getLowPrice()) / 4;	// 利用K线重心为计算依据
			index.set(++i % n);
			long total = LongStream.of(volArr).sum();
			if(total == 0) {
				return new TimeSeriesValue(0, bar.getActionTimestamp());
			}
			double weightedSum = 0;
			for(int j=0; j<n; j++) {
				weightedSum += priceArr[j] * volArr[j] / total;
			}
			return new TimeSeriesValue(weightedSum, bar.getActionTimestamp());
		};
	}

	/**
	 * RSV:=(CLOSE-LLV(LOW,N))/(HHV(HIGH,N)-LLV(LOW,N))*100;
	 * 收盘价与N周期最低值做差，N周期最高值与N周期最低值做差，两差之间做比值
	 * @param n		统计范围
	 * @return		返回计算函数
	 */
	static Function<BarField, TimeSeriesValue> RSV(int n){
		final double[] lowArr = new double[n];
		final double[] highArr = new double[n];
		final AtomicInteger index = new AtomicInteger(0);
		return bar -> {
			int i = index.get();
			lowArr[i] = bar.getLowPrice();
			highArr[i] = bar.getHighPrice();
			index.set(++i % n);
			double lowest = DoubleStream.of(lowArr).min().orElse(0);
			if(lowest == 0) {
				return new TimeSeriesValue(0, bar.getActionTimestamp());
			}
			double highest = DoubleStream.of(highArr).max().orElse(0);
			double rsv = (bar.getClosePrice() - lowest) / (highest - lowest) * 100;
			return new TimeSeriesValue(rsv, bar.getActionTimestamp());
		};
	}
	
	/**
	 * 指数加权平均EMA函数
	 * @param n		统计范围
	 * @return		返回计算函数
	 */
	static TimeSeriesUnaryOperator EMA(int n) {
		final AtomicDouble ema = new AtomicDouble();
		final AtomicBoolean hasInitVal = new AtomicBoolean();
		final double factor = 2D / (n + 1);
		return tv -> {
			double val = tv.getValue();
			long timestamp = tv.getTimestamp();
			if(hasInitVal.get()) {
				ema.set(factor * val + (1 - factor) * ema.get());
			} else {
				ema.set(val);
				hasInitVal.set(true);
			}
			return new TimeSeriesValue(ema.get(), timestamp);
		};
	}

	/**
	 * 简单移动平均SMA函数
	 * @param n		统计范围
	 * @return		返回计算函数
	 */
	static TimeSeriesUnaryOperator SMA(int n, int m) {
		final AtomicDouble sma = new AtomicDouble();
		final AtomicBoolean hasInitVal = new AtomicBoolean();
		final double factor = (double) m / n;
		return tv -> {
			double val = tv.getValue();
			long timestamp = tv.getTimestamp();
			if(hasInitVal.get()) {
				sma.set(factor * val + (1 - factor) * sma.get());
			} else {
				sma.set(val);
				hasInitVal.set(true);
			}
			return new TimeSeriesValue(sma.get(), timestamp);
		};
	}

	/**
	 * 简单移动平均MA函数
	 * @param n		统计范围
	 * @return		返回计算函数
	 */
	static TimeSeriesUnaryOperator MA(int n) {
		final double[] values = new double[n];
		final AtomicInteger cursor = new AtomicInteger();
		final AtomicDouble sumOfValues = new AtomicDouble();
		return tv -> {
			long timestamp = tv.getTimestamp();
			double val = tv.getValue();
			double oldVal = values[cursor.get()];
			values[cursor.get()] = val;
			cursor.set(cursor.incrementAndGet() % n);
			sumOfValues.addAndGet(val - oldVal);
			val = sumOfValues.get() / n;
			return new TimeSeriesValue(val, timestamp);
		};
	}

}
