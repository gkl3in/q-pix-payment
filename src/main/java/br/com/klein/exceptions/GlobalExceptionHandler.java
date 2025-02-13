package br.com.klein.exceptions;

import br.com.klein.model.ErrorResponse;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof QRCodeNotFoundException) {
            return handleQRCodeNotFoundException((QRCodeNotFoundException) exception);
        }

        return handleGenericException(exception);
    }

    private Response handleQRCodeNotFoundException(QRCodeNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .status(Response.Status.NOT_FOUND.getStatusCode())
                .error("QR Code Not Found")
                .message(exception.getMessage())
                .path(uriInfo.getPath())
                .build();

        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(errorResponse)
                .build();
    }

    private Response handleGenericException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .error("Internal Server Error")
                .message("Um erro inesperado ocorreu. Por favor, tente novamente mais tarde.")
                .path(uriInfo.getPath())
                .build();

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .build();
    }
}