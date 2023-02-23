package tech.quantit.northstar.gateway.okx;

import cn.hutool.core.lang.hash.Hash;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.w3.exchange.common.exceptions.ExchangeClientException;
import com.w3.exchange.common.exceptions.ExchangeConnectorException;
import com.w3.exchange.okx.impl.OKXSpotClientImpl;
import lombok.extern.slf4j.Slf4j;
import tech.quantit.northstar.gateway.api.IMarketCenter;

import java.util.HashMap;
import java.util.LinkedHashMap;


@Slf4j
public class OkxContractProvider {
	
	private IMarketCenter mktCenter;
	
	private OkxGatewaySettings settings;

	public OkxContractProvider(OkxGatewaySettings settings, IMarketCenter mktCenter) {
		this.mktCenter = mktCenter;
		this.settings = settings;
	}
	// 加载OKX网关合约
	public void loadContractOptions() {

//		OKXSpotClientImpl client = OKXSpotClientImpl.builder()
//				.apiKey(settings.getApiKey())
//				.secretKey(settings.getSecretKey())
//				.build();
//
//		try {
//			LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
//            // instType -》 SPOT：币币 MARGIN：币币杠杆 SWAP：永续合约 FUTURES：交割合约 OPTION：期权
//			parameters.put("instType", "FUTURES");
//            String result = client.createPubMarket().exchangeInfo(parameters);
//            JSONObject json = JSON.parseObject(result);
//            JSONArray symbols = json.getJSONArray("symbols");
//            for(int i=0; i<symbols.size(); i++) {
//            	JSONObject obj = symbols.getJSONObject(i);
//            	mktCenter.addInstrument(new OkxContract(obj));
//            }
//		} catch (ExchangeConnectorException e) {
//			log.error("fullErrMessage: {}", e.getMessage(), e);
//		} catch (ExchangeClientException e) {
//			log.error("fullErrMessage: {} \nerrMessage: {} \nerrCode: {} \nHTTPStatusCode: {}",
//					e.getMessage(), e.getErrMsg(), e.getErrorCode(), e.getHttpStatusCode(), e);
//		}
	}
}
