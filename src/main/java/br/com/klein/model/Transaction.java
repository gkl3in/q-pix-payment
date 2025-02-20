package br.com.klein.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@MongoEntity(collection = "transacao_pix")
public class Transaction extends PanacheMongoEntity {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getTipoChave() {
        return tipoChave;
    }

    public void setTipoChave(String tipoChave) {
        this.tipoChave = tipoChave;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }

    public StatusPix getStatus() {
        return status;
    }

    public void setStatus(StatusPix status) {
        this.status = status;
    }

    @Schema(description = "Identificador único da transação")
    private String id;
    @Schema(description = "Valor da transação")

    private BigDecimal valor;

    @Schema(description = "Tipo de Chave PIX")

    private String tipoChave;

    @Schema(description = "Chave PIX")
    private String chave;

    @Schema(description = "Linha Digitável PIX")
    private String linha;

    @Schema(description = "Status PIX")
    private StatusPix status;

    @Schema(description = "Data de Transação")
    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    private LocalDateTime data;
}
