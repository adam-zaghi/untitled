package com.dailycodework.gatewayservice.filter;

import com.dailycodework.gatewayservice.security.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        if (isPublicPath(path) || isPreflightRequest(exchange)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.isTokenValid(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        Claims claims = jwtUtil.extractClaims(token);

        String email = claims.getSubject();
        String userId = String.valueOf(claims.get("userId"));
        String role = String.valueOf(claims.get("role"));

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(builder -> builder
                        .header("X-User-Email", email)
                        .header("X-User-Id", userId)
                        .header("X-User-Role", role)
                )
                .build();

        return chain.filter(mutatedExchange);
    }

    private boolean isPublicPath(String path) {
        return path.contains("/api/auth/login")
                || path.contains("/api/auth/register")
                || path.contains("/actuator");
    }

    private boolean isPreflightRequest(ServerWebExchange exchange) {
        return exchange.getRequest().getMethod() == HttpMethod.OPTIONS;
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
///1. Est-ce que c’est /api/auth/login ou /api/auth/register ?
//oui → laisser passer
//non → continuer

//2. Est-ce qu’il y a un header Authorization ?
//non → 401

//        3. Est-ce que le token commence par Bearer ?
//non → 401

//      4. Est-ce que le token est valide ?
//non → 401

//       5. Si le token est valide :
//ajouter X-User-Id, X-User-Email, X-User-Role
//puis laisser passer vers le microservice