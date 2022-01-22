package tech.quantit.northstar.strategy.api.indicator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

import org.junit.jupiter.api.Test;

import tech.quantit.northstar.strategy.api.indicator.Indicator.ValueType;
import xyz.redtorch.pb.CoreField.BarField;

class ExpMovingAverageTest {
	String symbol = "rb2205";
	
	// 数据来源于RB2205在22年1月19日（包含18日夜盘）的一分钟数据
	double[] sample = new double[] {
			4632,4633,4638,4636,4632,4640,4637,4643,4649,4655,4660,4664,4672,4676,4676,4672,4669,4674,4668,4669,4671,4675,4679,4678,4679,4686,4683,4683,4680,4678,4684,4684,4684,4686,4687,4692,4693,4696,4702,4702,4697,4699,4697,4697,4699,4694,4698,4697,4695,4697,4694,4694,4692,4691,4694,4697,4698,4699,4702,4704,4696,4701,4704,4702,4704,4702,4704,4705,4701,4703,4706,4706,4706,4709,4712,4714,4717,4715,4718,4722,4722,4720,4720,4723,4719,4720,4721,4721,4721,4718,4723,4727,4731,4728,4726,4725,4723,4723,4723,4730,4731,4731,4730,4734,4733,4732,4734,4735,4735,4729,4733,4730,4725,4728,4720,4719,4716,4712,4711,4716,4713,4715,4714,4709,4705,4714,4720,4716,4717,4715,4716,4712,4709,4708,4712,4714,4721,4721,4719,4723,4729,4730,4738,4737,4737,4727,4730,4730,4725,4730,4732,4731,4731,4736,4733,4735,4735,4738,4733,4738,4737,4738,4733,4732,4737,4740,4737,4736,4738,4733,4735,4734,4734,4733,4732,4730,4726,4729,4725,4729,4721,4720,4717,4720,4722,4723,4725,4725,4725,4725,4720,4719,4718,4722,4724,4726,4719,4720,4719,4716,4719,4722,4722,4721,4721,4721,4717,4720,4717,4713,4715,4715,4715,4714,4713,4712,4711,4709,4708,4712,4715,4715,4716,4720,4718,4716,4719,4718,4718,4716,4719,4718,4715,4717,4717,4713,4713,4712,4713,4709,4710,4708,4711,4705,4704,4699,4695,4702,4706,4712,4712,4706,4708,4706,4706,4697,4699,4701,4696,4697,4695,4693,4687,4692,4687,4684,4674,4670,4676,4675,4678,4675,4679,4679,4678,4681,4683,4687,4689,4687,4691,4690,4693,4691,4689,4686,4693,4693,4698,4696,4695,4705,4705,4704,4708,4706,4713,4712,4711,4711,4708,4706,4709,4707,4716,4716,4711,4710,4713,4717,4714,4710,4712,4712,4713,4706,4711,4713,4711,4715,4715,4716,4715,4715,4717,4717,4720,4714,4711,4712,4710,4714,4713,4714,4713,4714,4711,4707,4709,4708,4710,4701,4702,4704,4713
	};
	
	double[] ema10results = new double[] {4710.66,4708.9,4707.65,4706.98,4708.08};
	double[] ema20results = new double[] {4711.54,4710.54,4709.73,4709.18,4709.55};
	double[] ema12results = new double[] {4710.97,4709.43,4708.29,4707.63,4708.46};
	double[] ema26results = new double[] {4711.44,4710.67,4710.03,4709.58,4709.83};

	@Test
	void test() {
		Indicator ema10 = new ExpMovingAverage(symbol, 10, ValueType.CLOSE);
		Indicator ema20 = new ExpMovingAverage(symbol, 20, ValueType.CLOSE);
		Indicator ema12 = new ExpMovingAverage(symbol, 12, ValueType.CLOSE);
		Indicator ema26 = new ExpMovingAverage(symbol, 26, ValueType.CLOSE);
		for(double v : sample) {
			BarField bar = BarField.newBuilder().setUnifiedSymbol(symbol).setClosePrice(v).build();
			ema10.onBar(bar);
			ema12.onBar(bar);
			ema26.onBar(bar);
			ema20.onBar(bar);
		}
		for(int i=0; i<5; i++) {
			assertThat(ema10.value(i)).isCloseTo(ema10results[4 - i], offset(1e-2));
			assertThat(ema20.value(i)).isCloseTo(ema20results[4 - i], offset(1e-2));
			assertThat(ema12.value(i)).isCloseTo(ema12results[4 - i], offset(1e-2));
			assertThat(ema26.value(i)).isCloseTo(ema26results[4 - i], offset(1e-2));
		}
	}

}
