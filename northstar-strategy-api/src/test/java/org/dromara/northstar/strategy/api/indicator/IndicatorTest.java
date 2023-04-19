package org.dromara.northstar.strategy.api.indicator;

import static org.assertj.core.api.Assertions.assertThat;

import org.dromara.northstar.common.model.TimeSeriesValue;
import org.dromara.northstar.strategy.api.indicator.Indicator;
import org.dromara.northstar.strategy.api.indicator.Indicator.Configuration;
import org.dromara.northstar.strategy.api.indicator.Indicator.ValueType;
import org.junit.jupiter.api.Test;

import test.common.TestFieldFactory;

class IndicatorTest {
	
	TestFieldFactory factory = new TestFieldFactory("testGateway");
	
	Indicator indicator = new Indicator(Configuration.builder()
			.indicatorName("test")
			.bindedContract(factory.makeContract("rb2210"))
			.indicatorRefLength(5)
			.build(), ValueType.CLOSE, tv -> tv);

	@Test
	void testValue() {
		indicator.updateVal(new TimeSeriesValue(5000, 123456789));
		assertThat(indicator.value(0)).isEqualTo(5000);
	}

	@Test
	void testValueWithTime() {
		indicator.updateVal(new TimeSeriesValue(5000, 123456789));
		assertThat(indicator.timeSeriesValue(0).getValue()).isEqualTo(5000);
	}

	@Test
	void testValueOn() {
		indicator.updateVal(new TimeSeriesValue(5000, 123456789));
		assertThat(indicator.valueOn(indicator.timeSeriesValue(0).getTimestamp())).hasValue(5000D);
	}
	
	@Test
	void testIsReady() {
		for(int i=0; i<10; i++) {
			indicator.updateVal(new TimeSeriesValue(5000, 123456789 + i * 100));
			if(i>=4) {
				assertThat(indicator.isReady()).isTrue();
			} else {
				assertThat(indicator.isReady()).isFalse();
			}
		}
	}

	@Test
	void testBindedUnifiedSymbol() {
		assertThat(indicator.bindedUnifiedSymbol()).isEqualTo("rb2210@SHFE@FUTURES");
	}

	@Test
	void testHighestPosition() {
		double[] data = new double[] {5000D, 5001D, 4988D, 5050D, 5022D};
		for(int i=0; i<5; i++) {
			indicator.updateVal(new TimeSeriesValue(data[i], 123456789 + i * 100, i==4));
		}
		assertThat(indicator.highestPosition()).isEqualTo(1);
	}

	@Test
	void testLowestPosition() {
		double[] data = new double[] {5000D, 5001D, 4988D, 5050D, 5022D};
		for(int i=0; i<5; i++) {
			indicator.updateVal(new TimeSeriesValue(data[i], 123456789 + i * 100, i==4));
		}
		assertThat(indicator.lowestPosition()).isEqualTo(2);
	}

	@Test
	void testGetData() {
		double[] data = new double[] {5000D, 5001D, 4988D, 5050D, 5022D};
		for(int i=0; i<5; i++) {
			indicator.updateVal(new TimeSeriesValue(data[i], 123456789 + i * 100, i==4));
		}
		assertThat(indicator.getData()).hasSize(5);
	}

}