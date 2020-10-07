package tech.xuanwu.northstar.trader.domain.event;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import tech.xuanwu.northstar.constant.GatewayLifecycleEvent;
import tech.xuanwu.northstar.gateway.FastEventEngine;
import tech.xuanwu.northstar.gateway.FastEventEngine.EventType;
import tech.xuanwu.northstar.gateway.FastEventEngine.FastEvent;
import tech.xuanwu.northstar.gateway.FastEventEngine.FastEventHandler;
import tech.xuanwu.northstar.persistance.AccountRepo;
import tech.xuanwu.northstar.persistance.po.Account;
import tech.xuanwu.northstar.gateway.GatewayApi;
import tech.xuanwu.northstar.trader.constants.Constants;
import tech.xuanwu.northstar.trader.domain.contract.IndexContractMaker;
import tech.xuanwu.northstar.trader.domain.data.AccountInfoRecorder;
import tech.xuanwu.northstar.trader.domain.simulated.SimulatedGateway;
import xyz.redtorch.pb.CoreEnum.ConnectStatusEnum;
import xyz.redtorch.pb.CoreEnum.ProductClassEnum;
import xyz.redtorch.pb.CoreField.ContractField;
import xyz.redtorch.pb.CoreField.GatewayField;


@Slf4j
@Component
public class GatewayLifecycleHandler implements FastEventHandler{
	
	@Autowired
	private FastEventEngine feEngine;
	
	@Autowired
	@Qualifier(Constants.TRADABLE_ACCOUNT_PROFILE)
	private Map<String, GatewayField> gatewayMap;
	
	@Autowired
	@Qualifier(Constants.TRADABLE_ACCOUNT)
	private Map<String, GatewayApi> gatewayApiMap;
	
	@Autowired
	@Qualifier(Constants.CTP_MARKETDATA)
	private GatewayApi ctpMktDataGateway;
	
	@Autowired
	@Qualifier(Constants.CONTRACT_MAP)
	private Map<String, ContractField> contractMap;
	
	@Autowired
	private AccountInfoRecorder accountInfoRecorder;
	
	@Autowired
	AccountRepo accountRepo;
	
	@Autowired
	private IndexContractMaker indexContractMaker;
	
	@PostConstruct
	private void register() throws IOException {
		feEngine.addHandler(this);
	}

	@Override
	public void onEvent(FastEvent event, long sequence, boolean endOfBatch) throws Exception {
		EventType eventType = event.getEventType();
		String content = event.getEvent();
		Object obj = event.getObj();
		if(eventType != EventType.LIFECYCLE) {
			return;
		}
		
		String gatewayId;
		switch(content) {
		case GatewayLifecycleEvent.BEFORE_GATEWAY_CONNECT:
			gatewayId = (String) obj;
			if(!gatewayMap.containsKey(gatewayId)) {
				return;
			}
			gatewayMap.put(gatewayId, gatewayMap.get(gatewayId).toBuilder().setStatus(ConnectStatusEnum.CS_Connecting).build());
			// TODO 邮件通知
			break;
		case GatewayLifecycleEvent.ON_GATEWAY_CONNECTED:
			gatewayId = (String) obj;
			if(!gatewayMap.containsKey(gatewayId)) {
				return;
			}
			gatewayMap.put(gatewayId, gatewayMap.get(gatewayId).toBuilder().setStatus(ConnectStatusEnum.CS_Connected).build());
			// TODO 邮件通知
			break;
		case GatewayLifecycleEvent.ON_GATEWAY_READY:
			String[] params = (String[]) obj;
			gatewayId = params[0];
			// TODO 邮件通知
			break;
		case GatewayLifecycleEvent.ON_CTP_CONTRACT_READY:
			Map<String, ContractField> contractMapSrc = (Map<String, ContractField>) obj;
			indexContractMaker.initFrom(contractMapSrc);
			if(!ctpMktDataGateway.isConnected()) {
				throw new IllegalStateException("CTP网关未连线");
			}
			// 默认订阅全市场期货合约
			for(Entry<String, ContractField> e : contractMapSrc.entrySet()) {
				ContractField c = e.getValue();
				if(c.getProductClass() != ProductClassEnum.FUTURES) {
					continue;
				}
				ctpMktDataGateway.subscribe(c);
				contractMap.put(c.getSymbol(), c);
				contractMap.put(c.getUnifiedSymbol(), c);
			}
			
			break;
		case GatewayLifecycleEvent.ON_CTP_ACTION_REPLAY_DONE:
			// 初始化各个基础组件
			accountInfoRecorder.init();
			for(Entry<String, GatewayApi> e : gatewayApiMap.entrySet()) {
				if(e.getValue() instanceof SimulatedGateway) {
					Account account = accountRepo.findByGatewayId(e.getKey());
					SimulatedGateway simGateway = (SimulatedGateway)e.getValue();
					account = simGateway.getSimMarket().init(account);
					accountRepo.save(account);
				}
			}
			break;
		case GatewayLifecycleEvent.ON_GATEWAY_DISCONNECTED:
			gatewayId = (String) obj;
			gatewayMap.put(gatewayId, gatewayMap.get(gatewayId).toBuilder().setStatus(ConnectStatusEnum.CS_Disconnected).build());
			// TODO 邮件通知
			break;
		default:
			log.info("未处理事件：{}", content);
		}
	}

}
