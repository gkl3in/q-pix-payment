package br.com.klein.model;

import java.time.LocalDateTime;

public record Chave(TipoChave tipoChave, String chave, String ispb, TipoPessoa tipoPessoa,
                    String cpfCnpj,
                    String nome,
                    LocalDateTime dataHoraCriacao
                    ) {
}
