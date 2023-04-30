package com.marcusrehn.gqlInJava;

import com.example.graphql.model.RoleTO;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLFieldsContainer;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;

import java.util.Map;

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
        DataFetcher authDataFetcher = new DataFetcher() {
            @Override
            public Object get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
                Map<String, Object> contextMap = dataFetchingEnvironment.getContext();
                return originalDataFetcher.get(dataFetchingEnvironment);
            }
        };
        environment.setFieldDataFetcher(authDataFetcher);
        return SchemaDirectiveWiring.super.onField(environment);
    }
}
