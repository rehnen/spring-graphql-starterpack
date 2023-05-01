package com.marcusrehn.gqlInJava;

import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class RequestHeaderInterceptor implements WebGraphQlInterceptor {
    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        var roleHeader = request.getHeaders().get("role");
        if (roleHeader == null) {
            return chain.next(request);
        }
        request.configureExecutionInput(
                ((executionInput, builder) -> builder
                        .graphQLContext(Collections.singletonMap("role", roleHeader.get(0))).build())
        );
        return chain.next(request);
    }
}