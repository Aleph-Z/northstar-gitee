package tech.quantit.northstar.data.ds.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.quantit.northstar.common.IDataServiceManager;
import tech.quantit.northstar.common.constant.ChannelType;

import java.util.Map;

/**
 * 提供网关对应的具体数据服务对象
 */
@Component
@Slf4j
public class DataManagerFactory {

    /* gatewayType -> dataServiceManager */
	private  Map<ChannelType, IDataServiceManager> dmFactoryMap;

	public DataManagerFactory(Map<ChannelType, IDataServiceManager> dmFactoryMap) {
		this.dmFactoryMap = dmFactoryMap;
	}

	public IDataServiceManager getDsmFactory(ChannelType gatewayType) {
		if(!dmFactoryMap.containsKey(gatewayType)) {
			throw new IllegalStateException("不存在该网关类型：" + gatewayType);
		}
		return dmFactoryMap.get(gatewayType);
	}
}
