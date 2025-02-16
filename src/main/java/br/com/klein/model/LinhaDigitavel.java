package br.com.klein.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record LinhaDigitavel(

        @Schema(description = "Linha Digitável") String linha,
        @Schema(description = "UUID Gerado") String uuid) {
}
