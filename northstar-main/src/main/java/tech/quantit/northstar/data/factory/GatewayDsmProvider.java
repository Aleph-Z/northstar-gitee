package tech.quantit.northstar.data.factory;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.quantit.northstar.common.IDataServiceManager;
import tech.quantit.northstar.common.constant.ChannelType;
import tech.quantit.northstar.main.SpringContextUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 提供网关对应的具体数据服务对象
 */
@Component
@Slf4j
public class GatewayDsmProvider implements InitializingBean {

	@Autowired
	private SpringContextUtil springContextUtil;
	private static final String SPLIT_STR = "DATA";
    /* gatewayType -> dataServiceManager */
	private final  Map<ChannelType, IDataServiceManager> dsmFactoryMap = new ConcurrentHashMap<>();

	@Override
	public void afterPropertiesSet() throws Exception {
		springContextUtil.getBeanFactory().getBeansOfType(IDataServiceManager.class).forEach((k, v) -> {
			String gatewayId = k.toUpperCase().split(SPLIT_STR)[0].toUpperCase();
			try{
				dsmFactoryMap.put(StrUtil.isEmpty(gatewayId)?ChannelType.CTP:ChannelType.valueOf(gatewayId), v);
			}catch (Exception e){
				log.error("网关-数据服务初始化失败,请按网关数据服务类名规范命名【具体网关名称+DataServiceManager】");
			}

		});
	}
	public IDataServiceManager getDsmFactory(ChannelType gatewayType) {
		if(!dsmFactoryMap.containsKey(gatewayType)) {
			throw new IllegalStateException("不存在该网关类型：" + gatewayType);
		}
		return dsmFactoryMap.get(gatewayType);
	}
}
