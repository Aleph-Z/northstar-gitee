package tech.quantit.northstar.main.external;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;

import tech.quantit.northstar.main.external.DataServiceManager.DataSet;
import xyz.redtorch.pb.CoreField.BarField;

class DataServiceManagerTest {
	
	DataServiceManager mgr = new DataServiceManager();
	
	@Test
	void testDailyData() {
		mgr.rest = mock(RestTemplate.class);
		String data = "{\"fields\":[\"ns_code\",\"trade_date\",\"pre_close\",\"pre_settle\",\"open\",\"high\",\"low\",\"close\",\"settle\",\"change1\",\"change2\",\"vol\",\"amount\",\"oi\",\"oi_chg\"],\"items\":[[\"rb2205@SHFE\",\"20220215\",\"4817.0\",\"4862.0\",\"4805.0\",\"4816.0\",\"4666.0\",\"4728.0\",\"4744.0\",\"-134.0\",\"-118.0\",\"2124511.0\",\"10079013.32\",\"1921187.0\",\"32370.0\"],[\"rb2205@SHFE\",\"20220214\",\"4905.0\",\"4986.0\",\"4900.0\",\"4910.0\",\"4791.0\",\"4817.0\",\"4862.0\",\"-169.0\",\"-124.0\",\"1874764.0\",\"9116170.34\",\"1888817.0\",\"-6322.0\"]]}";
		DataSet dataSet = JSON.parseObject(data, DataSet.class);
		ResponseEntity<DataSet> mockResp = mock(ResponseEntity.class);
		when(mockResp.getBody()).thenReturn(dataSet);
		when(mockResp.getStatusCode()).thenReturn(HttpStatus.OK);
		when(mgr.rest.exchange(any(URI.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
			.thenReturn(mockResp);
		
		List<BarField> result = mgr.getMinutelyData("test", LocalDate.now(), LocalDate.now());
		assertThat(result).hasSize(2);
		assertThat(result.get(0).getActionDay()).isEqualTo("20220214");
		assertThat(result.get(0).getActionTime()).isEqualTo("09:00:00");
		assertThat(result.get(0).getTradingDay()).isEqualTo("20220214");
		assertThat(result.get(0).getActionTimestamp() > 0).isTrue();
		assertThat(result.get(0).getUnifiedSymbol()).isEqualTo("rb2205@SHFE");
	}
	
	@Test
	void testMinData() {
		mgr.rest = mock(RestTemplate.class);
		String data = "{\"fields\":[\"ns_code\",\"trade_time\",\"open\",\"close\",\"high\",\"low\",\"vol\",\"amount\",\"oi\"],\"items\":[[\"rb2205@SHFE\",\"2022-02-15 15:00:00\",\"4690.0\",\"4728.0\",\"4741.0\",\"4688.0\",\"243861.0\",\"11508913810.0\",\"1921187.0\"],[\"rb2205@SHFE\",\"2022-02-15 14:15:00\",\"4755.0\",\"4692.0\",\"4760.0\",\"4666.0\",\"750003.0\",\"35311449470.0\",\"1912611.0\"],[\"rb2205@SHFE\",\"2022-02-15 11:15:00\",\"4749.0\",\"4755.0\",\"4795.0\",\"4743.0\",\"246912.0\",\"11779822690.0\",\"1855328.0\"],[\"rb2205@SHFE\",\"2022-02-15 10:00:00\",\"4770.0\",\"4750.0\",\"4787.0\",\"4733.0\",\"317443.0\",\"15098072220.0\",\"1838341.0\"],[\"rb2205@SHFE\",\"2022-02-14 23:00:00\",\"4790.0\",\"4772.0\",\"4795.0\",\"4765.0\",\"142364.0\",\"6808136940.0\",\"1823296.0\"],[\"rb2205@SHFE\",\"2022-02-14 22:00:00\",\"4801.0\",\"4790.0\",\"4816.0\",\"4763.0\",\"419865.0\",\"20088510910.0\",\"1828685.0\"],[\"rb2205@SHFE\",\"2022-02-14 21:00:00\",\"4805.0\",\"4805.0\",\"4805.0\",\"4805.0\",\"4063.0\",\"195227150.0\",\"1887841.0\"]]}";
		DataSet dataSet = JSON.parseObject(data, DataSet.class);
		ResponseEntity<DataSet> mockResp = mock(ResponseEntity.class);
		when(mockResp.getBody()).thenReturn(dataSet);
		when(mockResp.getStatusCode()).thenReturn(HttpStatus.OK);
		when(mgr.rest.exchange(any(URI.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
			.thenReturn(mockResp);
		
		List<BarField> result = mgr.getMinutelyData("test", LocalDate.now(), LocalDate.now());
		assertThat(result).hasSize(7);
		assertThat(result.get(0).getActionDay()).isEqualTo("20220214");
		assertThat(result.get(0).getActionTime()).isEqualTo("21:00:00");
		assertThat(result.get(0).getTradingDay()).isEqualTo("20220215");
		assertThat(result.get(0).getActionTimestamp() > 0).isTrue();
		assertThat(result.get(0).getUnifiedSymbol()).isEqualTo("rb2205@SHFE");
	}

}
