package org.dromara.northstar.config;

import org.dromara.northstar.data.IGatewayRepository;
import org.dromara.northstar.data.IMailConfigRepository;
import org.dromara.northstar.data.IMarketDataRepository;
import org.dromara.northstar.data.IModuleRepository;
import org.dromara.northstar.data.IPlaybackRuntimeRepository;
import org.dromara.northstar.data.ISimAccountRepository;
import org.dromara.northstar.data.jdbc.GatewayDescriptionRepository;
import org.dromara.northstar.data.jdbc.GatewayRepoAdapter;
import org.dromara.northstar.data.jdbc.MailConfigDescriptionRepository;
import org.dromara.northstar.data.jdbc.MailConfigRepoAdapter;
import org.dromara.northstar.data.jdbc.MarketDataRepoAdapter;
import org.dromara.northstar.data.jdbc.MarketDataRepository;
import org.dromara.northstar.data.jdbc.ModuleDealRecordRepository;
import org.dromara.northstar.data.jdbc.ModuleDescriptionRepository;
import org.dromara.northstar.data.jdbc.ModuleRepoAdapter;
import org.dromara.northstar.data.jdbc.ModuleRuntimeDescriptionRepository;
import org.dromara.northstar.data.jdbc.PlaybackRuntimeRepoAdapter;
import org.dromara.northstar.data.jdbc.PlaybackRuntimeRepository;
import org.dromara.northstar.data.jdbc.SimAccountRepoAdapter;
import org.dromara.northstar.data.jdbc.SimAccountRepository;
import org.dromara.northstar.gateway.GatewayMetaProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    IGatewayRepository gatewayRepo(GatewayDescriptionRepository delelgate) {
    	return new GatewayRepoAdapter(delelgate);
    }
    
    @Bean
    IModuleRepository moduleRepo(ModuleDealRecordRepository mdrRepo, ModuleDescriptionRepository mdRepo, ModuleRuntimeDescriptionRepository mrdRepo) {
    	return new ModuleRepoAdapter(mdRepo, mrdRepo, mdrRepo);
    }
    
    @Bean
    IMailConfigRepository mailConfigRepo(MailConfigDescriptionRepository delegate) {
    	return new MailConfigRepoAdapter(delegate);
    }
    
    @Bean
    IPlaybackRuntimeRepository playbackRtRepo(PlaybackRuntimeRepository delegate) {
    	return new PlaybackRuntimeRepoAdapter(delegate);
    }
    
    @Bean
    ISimAccountRepository simAccountRepo(SimAccountRepository delegate) {
    	return new SimAccountRepoAdapter(delegate);
    }
    
    @Bean
    IMarketDataRepository marketDataRepo(MarketDataRepository mdRepo, GatewayMetaProvider pvd) {
    	return new MarketDataRepoAdapter(mdRepo, pvd);
    }
}
