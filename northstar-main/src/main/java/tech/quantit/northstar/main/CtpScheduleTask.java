package tech.quantit.northstar.main;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import tech.quantit.northstar.domain.gateway.GatewayAndConnectionManager;
import tech.quantit.northstar.domain.gateway.GatewayConnection;
import tech.quantit.northstar.gateway.api.Gateway;
import tech.quantit.northstar.main.utils.HolidayManager;

@Slf4j
@Component
public class CtpScheduleTask {
	
	@Autowired
	private GatewayAndConnectionManager gatewayConnMgr;
	
	@Autowired
	private HolidayManager holidayMgr;
	
	@Value("${spring.profiles.active}")
	private String profile;

	@Scheduled(cron="0 0/1 0-1,9-14,21-23 ? * 1-5")
	public void timelyCheckConnection() {
		if(holidayMgr.isHoliday(LocalDateTime.now())) {
			return;
		}
		connectIfNotConnected();
		log.debug("开盘时间连线巡检");
	}
	
	@Scheduled(cron="0 55 8,20 ? * 1-5")
	public void dailyCheckConnection() {
		if(holidayMgr.isHoliday(LocalDateTime.now())) {
			log.debug("当前为假期，不进行连线");
			return;
		}
		connectIfNotConnected();
		log.info("日连线定时任务");
	}
	
	private void connectIfNotConnected() {
		for(GatewayConnection conn : gatewayConnMgr.getAllConnections()) {
			if(conn.isConnected() || !conn.getGwDescription().isAutoConnect()) {
				continue;
			}
			Gateway gateway = gatewayConnMgr.getGatewayByConnection(conn);
			gateway.connect();
			log.info("网关[{}]，自动连线", conn.getGwDescription().getGatewayId());
		}
	}
	
	@Scheduled(cron="0 1 15 ? * 1-5")
	public void dailySettlement() {
		if(holidayMgr.isHoliday(LocalDateTime.now())) {
			log.debug("当前为假期，没有结算任务");
			return;
		}
		log.info("日结算定时任务");
	}

}
