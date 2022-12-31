package tech.quantit.northstar.gateway.tiger;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tigerbrokers.stock.openapi.client.struct.enums.License;

import lombok.Getter;
import lombok.Setter;
import tech.quantit.northstar.common.constant.FieldType;
import tech.quantit.northstar.common.model.DynamicParams;
import tech.quantit.northstar.common.model.GatewaySettings;
import tech.quantit.northstar.common.model.Setting;
import tech.quantit.northstar.gateway.api.GatewayMetaProvider;

@Getter
@Setter
@Component
public class TigerGatewaySettings extends DynamicParams implements GatewaySettings {

	@Autowired
	private GatewayMetaProvider pvd;
	
	@Setting(label="用户ID", order=10, type=FieldType.TEXT)
	private String tigerId;
	
	@Setting(label="账户ID", order=20, type=FieldType.TEXT)
	private String accountId;
	
	@Setting(label="RSA私钥", order=30, type=FieldType.TEXT)
	private String privateKey;
	
	@Setting(label="证书类型", order=40, type=FieldType.SELECT, optionsVal = {"TBNZ", "TBSG"})
	private License license;
	
	@Setting(label="secretKey", order=50, type=FieldType.TEXT)
	private String secretKey;
	
}
