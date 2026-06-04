package com.codingSuman.ecommerce.api_gateway.filters;

import com.codingSuman.ecommerce.api_gateway.service.JWTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config>
{
    private JWTService jwtService;

    public AuthenticationGatewayFilterFactory(JWTService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config)
    {
        return (((exchange, chain) -> {

//            if(!config.isEnabled)
//            {
//                return chain.filter(exchange);
//            }

            String headers = exchange.getRequest().getHeaders().getFirst("Authorization");
            if(headers == null)
            {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            String token = headers.split(" ")[1];

            Long UserId = jwtService.getUserIdFromToken(token);

            exchange.getRequest()
                    .mutate()
                    .header("X-User-Id", UserId.toString())
                    .build();


           return chain.filter(exchange);
        }));
    }

    public static class Config
    {
        private boolean isEnabled;

    }
}
