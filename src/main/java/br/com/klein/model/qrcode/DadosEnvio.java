package br.com.klein.model.qrcode;

import java.math.BigDecimal;

import br.com.klein.model.Chave;

import static java.util.Objects.requireNonNull;


public record DadosEnvio(Chave chavePIX, BigDecimal valor, String cidadeRemetente, String descricao) {

    public static final int TAMANHO_NOME = 25;
    public static final int TAMANHO_CHAVE = 77;
    public static final int TAMANHO_CIDADE = 15;
    public static final int TAMANHO_DESCRICAO = 72;
    public static final int TAMANHO_VALOR = 13;

    public DadosEnvio(Chave chavePIX, BigDecimal valor, String cidadeRemetente) {
        this(chavePIX, valor, cidadeRemetente, "");
    }

    public DadosEnvio {
        if(requireNonNull(chavePIX.nome()).isBlank())
            throw new IllegalArgumentException("O nome do destinatário é obrigatório.");
        var nomeDestinatario = chavePIX.nome();
        var chaveDestinatario = chavePIX.chave();
        if(chavePIX.nome().length() > TAMANHO_NOME) {
            final var msg = "Nome do destinatário não pode ter mais que 25 caracteres. '%s' tem %d caracteres."
                    .formatted(nomeDestinatario, nomeDestinatario.length());
            throw new IllegalArgumentException(msg);
        }

        if(requireNonNull(chaveDestinatario).isBlank())
            throw new IllegalArgumentException("A chave PIX do destinatário é obrigatória.");
        chaveDestinatario = chaveDestinatario.trim();
        if(chaveDestinatario.length() > TAMANHO_CHAVE) {
            final var msg = "Chave PIX do destinatário não pode ter mais que 77 caracteres. '%s' tem %d caracteres."
                    .formatted(chaveDestinatario, chaveDestinatario.length());
            throw new IllegalArgumentException(msg);
        }

        if(requireNonNull(cidadeRemetente).isBlank())
            throw new IllegalArgumentException("A cidade do remetente é obrigatória.");
        cidadeRemetente = cidadeRemetente.trim();
        if(cidadeRemetente.length() > TAMANHO_CIDADE) {
            final var msg = "Cidade do remetente não pode ter mais que 15 caracteres. '%s' tem %d caracteres."
                    .formatted(cidadeRemetente, cidadeRemetente.length());
            throw new IllegalArgumentException(msg);
        }

        requireNonNull(descricao, "A descrição não pode ser nula. Informe um texto vazio no lugar.");
        descricao = descricao.trim();
        if(descricao.length() > TAMANHO_DESCRICAO) {
            final var msg = "Descrição não pode ter mais que 72 caracteres. '%s' tem %d caracteres."
                    .formatted(descricao, descricao.length());
            throw new IllegalArgumentException(msg);
        }

        if(valor.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("O valor do PIX deve ser maior que zero.");

        final var valorStr = formatNumber(valor);
        if(valorStr.length() > TAMANHO_VALOR) {
            final var msg = "Valor não pode ter mais que 13 caracteres. '%s' tem %d caracteres."
                    .formatted(valorStr, valorStr.length());
            throw new IllegalArgumentException(msg);
        }
    }

    public String valorStr(){
        return formatNumber(valor);
    }

    private static String formatNumber(final BigDecimal value){
        return String.format("%.2f", value).formatted().replace(",", ".");
    }
}
