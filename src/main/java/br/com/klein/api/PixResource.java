package br.com.klein.api;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.Objects;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import br.com.klein.model.LinhaDigitavel;
import br.com.klein.model.Pix;
import br.com.klein.service.DictService;
import br.com.klein.service.PixService;

@Path("/v1/pix")
public class PixResource {

    @Inject
    DictService dictService;

    @Inject
    PixService pixService;

    @Operation(description = "API para criar uma linha digitável")
    @APIResponseSchema(LinhaDigitavel.class)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK"),
            @APIResponse(responseCode = "201", description = "Retorno OK com a linha criada."),
            @APIResponse(responseCode = "401", description = "Erro de autenticação dessa API"),
            @APIResponse(responseCode = "403", description = "Erro de autorização dessa API"),
            @APIResponse(responseCode = "404", description = "Recurso não encontrado")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/linha")
    public Response gerarLinhaDigitavel(final Pix pix) {
        var chave = dictService.buscarChave(pix.chave());

        if (Objects.nonNull(chave)) {
            return Response.ok(pixService.gerarLinhaDigitavel(chave, pix.valor(), pix.cidadeRemetente())).build();
        }

        return null;
    }

    @Operation(description = "API para buscar um QRCode a partir de um UUID específico.")
    @APIResponseSchema(Response.class)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK"),
            @APIResponse(responseCode = "201", description = "Retorno OK com a transação criada."),
            @APIResponse(responseCode = "401", description = "Erro de autenticação dessa API"),
            @APIResponse(responseCode = "403", description = "Erro de autorização dessa API"),
            @APIResponse(responseCode = "404", description = "Recurso não encontrado")
    })
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("image/png")
    @Path("/qrcode/{uuid}")
    public Response qrCode(@PathParam("uuid") String uuid) throws IOException {
        return Response.ok(pixService.gerarQrCode(uuid)).build();
    }
}
