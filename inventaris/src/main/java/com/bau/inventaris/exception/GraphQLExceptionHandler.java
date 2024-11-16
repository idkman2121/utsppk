package com.bau.inventaris.exception;

import graphql.GraphQLError;
import graphql.GraphQLException;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {
    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
    if (ex instanceof InsufficientStockException || ex instanceof UserNotFoundException || ex instanceof InvalidCredentialsException) {
        return GraphqlErrorBuilder.newError(env)
                .message(ex.getMessage())
                .build();
    }
    return super.resolveToSingleError(ex, env);
}

}

