package tech.quantit.northstar.gateway.api;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

import tech.quantit.northstar.common.constant.GatewayType;

@Retention(RUNTIME)
@Target(TYPE)
@Component
public @interface ContractProviderComponent {

	GatewayType value();
}
