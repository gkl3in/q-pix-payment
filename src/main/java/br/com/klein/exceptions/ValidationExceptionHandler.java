package br.com.klein.exceptions;

import br.com.klein.model.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        String message = exception.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .reduce((msg1, msg2) -> msg1 + "; " + msg2)
                .orElse("Erro de validação");

        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .status(Response.Status.BAD_REQUEST.getStatusCode())
                .error("Validation Error")
                .message(message)
                .path(uriInfo.getPath())
                .build();

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .build();
    }
}