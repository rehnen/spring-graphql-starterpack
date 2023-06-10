package com.marcusrehn.gqlInJava;

import com.example.graphql.model.RoleTO;
import graphql.GraphQLContext;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLFieldsContainer;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import java.util.Map;

// this class is here to manage the authentication wiring
public class AuthWiring implements SchemaDirectiveWiring {
    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment) {
        if(environment.getAppliedDirective("auth") == null
                || environment.getAppliedDirective("auth").getArgument("role") == null) {
            return SchemaDirectiveWiring.super.onField(environment);
        }
        RoleTO role = RoleTO.valueOf(environment.getAppliedDirective("auth").getArgument("role").getValue());
        GraphQLFieldDefinition field = environment.getElement();
        GraphQLFieldsContainer parentType = environment.getFieldsContainer();
        //
        // build a data fetcher that first checks authorisation roles before then calling the original data fetcher
        //
        DataFetcher originalDataFetcher = environment.getCodeRegistry().getDataFetcher(parentType, field);
        DataFetcher authDataFetcher = dataFetchingEnvironment -> {
            GraphQLContext contextMap = dataFetchingEnvironment.getGraphQlContext();
            // TODO Find better way of accessing the security context
            SecurityContext user = contextMap.get( org.springframework.security.core.context.SecurityContext.class.getName() );

            return user.getAuthentication().getAuthorities().
                    stream().filter(p -> p.getAuthority().equals("ROLE_ADMIN")).findFirst().isPresent() ?
                    originalDataFetcher.get(dataFetchingEnvironment) : null;
        };
        environment.setFieldDataFetcher(authDataFetcher);
        return SchemaDirectiveWiring.super.onField(environment);
    }
}
