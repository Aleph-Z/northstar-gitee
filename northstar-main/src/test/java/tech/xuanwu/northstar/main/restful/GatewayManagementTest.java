package tech.xuanwu.northstar.main.restful;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.alibaba.fastjson.JSON;

import common.TestGatewayFactory;
import common.TestMongoUtils;
import tech.xuanwu.northstar.common.constant.GatewayType;
import tech.xuanwu.northstar.common.constant.GatewayUsage;
import tech.xuanwu.northstar.common.constant.ReturnCode;
import tech.xuanwu.northstar.common.model.CtpSettings;
import tech.xuanwu.northstar.common.model.GatewayDescription;
import tech.xuanwu.northstar.common.model.NsUser;
import tech.xuanwu.northstar.common.model.SimSettings;
import tech.xuanwu.northstar.engine.broadcast.SocketIOMessageEngine;
import tech.xuanwu.northstar.main.NorthstarApplication;

/**
 * GatewayManagement接口黑盒测试类
 * @author KevinHuangwl
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NorthstarApplication.class, value="spring.profiles.active=test")
@AutoConfigureMockMvc
public class GatewayManagementTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	private MockHttpSession session;
	
	@MockBean
	private SocketIOMessageEngine msgEngine;
	
	@Before
	public void setUp() throws Exception {
		session = new MockHttpSession();
		mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON_UTF8).content(JSON.toJSONString(new NsUser("admin","123456"))).session(session))
			.andExpect(status().isOk());
	}
	
	@After
	public void tearDown() {
		TestMongoUtils.clearDB();
	}
	
	@Test
	public void shouldFailWithoutAuth() throws Exception {
		GatewayDescription gatewayDes = TestGatewayFactory.makeMktGateway("testGateway", GatewayType.CTP, TestGatewayFactory.makeGatewaySettings(CtpSettings.class), false);
		mockMvc.perform(post("/mgt/gateway").contentType(MediaType.APPLICATION_JSON_UTF8).content(JSON.toJSONString(gatewayDes)))
			.andExpect(status().is(401));
	}

	@Test
	public void shouldCreateGateway() throws Exception {
		GatewayDescription gatewayDes = TestGatewayFactory.makeMktGateway("TG1", GatewayType.CTP, TestGatewayFactory.makeGatewaySettings(CtpSettings.class),false);
		mockMvc.perform(post("/mgt/gateway").contentType(MediaType.APPLICATION_JSON_UTF8).content(JSON.toJSONString(gatewayDes)).session(session))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(ReturnCode.SUCCESS));
	}
	
	@Test
	public void shouldFindCreatedGateway() throws Exception {
		shouldCreateGateway();
		
		mockMvc.perform(get("/mgt/gateway?usage=" + GatewayUsage.MARKET_DATA).session(session))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(ReturnCode.SUCCESS));
	}

	@Test
	public void shouldUpdateGateway() throws Exception {
		shouldCreateGateway();
		
		GatewayDescription gatewayDes = TestGatewayFactory.makeMktGateway("TG1", GatewayType.CTP, TestGatewayFactory.makeGatewaySettings(CtpSettings.class),false);
		mockMvc.perform(put("/mgt/gateway").contentType(MediaType.APPLICATION_JSON_UTF8).content(JSON.toJSONString(gatewayDes)).session(session))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(ReturnCode.SUCCESS));
	}
	
	@Test
	public void shouldRemoveGateway() throws Exception {
		shouldCreateGateway();
		
		mockMvc.perform(delete("/mgt/gateway?gatewayId=TG1").session(session))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(ReturnCode.SUCCESS));
	}
	
	@Test
	public void shouldFailIfNotProvidingSetting() throws Exception {
		GatewayDescription gwDes = TestGatewayFactory.makeMktGateway("TG1", GatewayType.CTP, null,false);
		mockMvc.perform(post("/mgt/gateway").contentType(MediaType.APPLICATION_JSON).content(JSON.toJSONString(gwDes)).session(session))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(ReturnCode.ERROR));
	}
	
	// @Test
	public void shouldSuccessWhenGettingActiveState() throws Exception {
		shouldCreateGateway();
		
		mockMvc.perform(get("/mgt/gateway/active?gatewayId=TG1").session(session))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(ReturnCode.SUCCESS));
	}
	
	@Test
	public void shouldSuccessWhenConnecting() throws Exception {
		shouldCreateGateway();
		
		mockMvc.perform(get("/mgt/connection?gatewayId=TG1").session(session))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(ReturnCode.SUCCESS));
	}
	
	@Test
	public void shouldSuccessWhenDisconnecting() throws Exception {
		shouldCreateGateway();
		
		mockMvc.perform(delete("/mgt/connection?gatewayId=TG1").session(session))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(ReturnCode.SUCCESS));
	}
	
	@Test
	public void shouldFailIfGatewayNotFound() throws Exception {
		mockMvc.perform(get("/mgt/connection?gatewayId=ANY").session(session))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(ReturnCode.NO_SUCH_ELEMENT_EXCEPTION));
		
		mockMvc.perform(delete("/mgt/connection?gatewayId=ANY").session(session))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(ReturnCode.NO_SUCH_ELEMENT_EXCEPTION));
	}
	
	@Test
	public void shouldIncreaseBalance() throws Exception {
		shouldCreateGateway();
		
		GatewayDescription gwDes = TestGatewayFactory.makeTrdGateway("TG2", "", GatewayType.SIM, TestGatewayFactory.makeGatewaySettings(SimSettings.class), false);
		
		mockMvc.perform(post("/mgt/gateway").contentType(MediaType.APPLICATION_JSON).content(JSON.toJSONString(gwDes)).session(session))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(ReturnCode.SUCCESS));
		
		mockMvc.perform(post("/mgt/moneyio?gatewayId=TG2&money=10000").session(session))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(ReturnCode.SUCCESS));
	}
	
	@Test
	public void shouldDecreaseBalance() throws Exception {
		shouldIncreaseBalance();
		
		mockMvc.perform(post("/mgt/moneyio?gatewayId=TG2&money=-10000").session(session))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(ReturnCode.SUCCESS));
	}
	
	@Test
	public void shouldFailIfNotSimGateway() throws Exception {
		shouldCreateGateway();
		
		mockMvc.perform(post("/mgt/moneyio?gatewayId=TG1&money=10000").session(session))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(ReturnCode.ERROR));
	}
}
