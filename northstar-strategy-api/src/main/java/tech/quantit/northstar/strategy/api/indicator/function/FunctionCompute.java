package tech.quantit.northstar.strategy.api.indicator.function;

import java.util.Objects;

import tech.quantit.northstar.common.model.TimeSeriesValue;
import tech.quantit.northstar.strategy.api.indicator.TimeSeriesUnaryOperator;

/**
 * 多函数计算
 * @author KevinHuangwl
 *
 */
public interface FunctionCompute {

	/**
	 * 函数相加
	 * @param fn1
	 * @param fn2
	 * @return
	 */
	static TimeSeriesUnaryOperator add(TimeSeriesUnaryOperator fn1, TimeSeriesUnaryOperator fn2){
		Objects.requireNonNull(fn1);
		Objects.requireNonNull(fn2);
		return tv -> {
			TimeSeriesValue v = fn1.apply(tv);
			TimeSeriesValue v0 = fn2.apply(tv);
			v.setValue(v.getValue() + v0.getValue());
			return v;
		};
	}

	/**
	 * 函数相减
	 * @param fn1
	 * @param fn2
	 * @return
	 */
	static TimeSeriesUnaryOperator minus(TimeSeriesUnaryOperator fn1, TimeSeriesUnaryOperator fn2){
		Objects.requireNonNull(fn1);
		Objects.requireNonNull(fn2);
		return tv -> {
			TimeSeriesValue v = fn1.apply(tv);
			TimeSeriesValue v0 = fn2.apply(tv);
			v.setValue(v.getValue() - v0.getValue());
			return v;
		};
	}

	/**
	 * 布林上轨
	 * @param mid
	 * @param std
	 * @param k
	 * @return
	 */
	static TimeSeriesUnaryOperator upper(TimeSeriesUnaryOperator mid, TimeSeriesUnaryOperator std ,int k){
		Objects.requireNonNull(mid);
		Objects.requireNonNull(std);
		return tv -> {
			TimeSeriesValue v = mid.apply(tv);
			TimeSeriesValue v0 = std.apply(tv);
			v.setValue(v.getValue() + k * v0.getValue());
			return v;
		};
	}

	/**
	 * 布林下轨
	 * @param mid
	 * @param std
	 * @param k
	 * @return
	 */
	static TimeSeriesUnaryOperator lower(TimeSeriesUnaryOperator mid, TimeSeriesUnaryOperator std ,int k){
		Objects.requireNonNull(mid);
		Objects.requireNonNull(std);
		return tv -> {
			TimeSeriesValue v = mid.apply(tv);
			TimeSeriesValue v0 = std.apply(tv);
			v.setValue(v.getValue() - k * v0.getValue());
			return v;
		};
	}
}
