package com.buildings.graphql.resolver.exception;

import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GraphQLExceptionHandler implements DataFetcherExceptionResolver{
  
  @Override
  public Mono<List<GraphQLError>> resolveException(Throwable exception, DataFetchingEnvironment environment) {
    List<GraphQLError> errors = new ArrayList<>();

    if (exception instanceof IllegalArgumentException) {
      GraphQLError error = GraphQLError.newError()
          .message(exception.getMessage())
          .path(environment.getExecutionStepInfo().getPath())
          .location(environment.getField().getSourceLocation())
          .extensions(Map.of("code", "BAD_USER_INPUT"))
          .build();
      
      errors.add(error);

    } else if (exception instanceof EntityNotFoundException) {
      GraphQLError error = GraphQLError.newError()
          .message(exception.getMessage())
          .path(environment.getExecutionStepInfo().getPath())
          .location(environment.getField().getSourceLocation())
          .extensions(Map.of("code", "NOT_FOUND"))
          .build();
      
      errors.add(error);

    } else {
      log.error("Unexpected error in GraphQL query", exception);
      GraphQLError error = GraphQLError.newError()
          .message("Internal server error")
          .path(environment.getExecutionStepInfo().getPath())
          .extensions(Map.of("code", "INTERNAL_SERVER_ERROR"))
          .build();
          
      errors.add(error);
    }

    return Mono.just(errors);
  }
}
