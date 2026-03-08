package com.buildings.graphql.resolver.exception;

import graphql.GraphQLError;
import graphql.ErrorType;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j

public class GraphQLExceptionHandler implements DataFetcherExceptionResolver{
  
  @Override
  public Mono<List<GraphQLError>> resolveException(Throwable exception, DataFetchingEnvironment environment) {
    List<GraphQLError> errors = new ArrayList<>();

    if (exception instanceof IllegalArgumentException) {
      GraphQLError error = GraphQLError.newError()
          .message(exception.getMessage())
          .errorType(ErrorType.ValidationError)
          .path(environment.getExecutionStepInfo().getPath())
          .location(environment.getField().getSourceLocation())
          .build();
      
      errors.add(error);

    } else {
      log.error("Unexpected error in GraphQL query", exception);
      GraphQLError error = GraphQLError.newError()
          .message("Internal server error")
          .path(environment.getExecutionStepInfo().getPath())
          .build();
          
      errors.add(error);
    }

    return Mono.just(errors);
  }
}
