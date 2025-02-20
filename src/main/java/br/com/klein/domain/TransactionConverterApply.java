package br.com.klein.domain;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Date;

import org.bson.Document;
import org.bson.types.Decimal128;

import br.com.klein.model.StatusPix;
import br.com.klein.model.Transaction;

import static br.com.klein.repository.TransacaoPixMongoClientRepository.AMERICA_SAO_PAULO;

public class TransactionConverterApply {
    public static final String ID = "_id";
    public static final String VALOR = "valor";
    public static final String STATUS = "status";
    public static final String LINHA = "linha";
    public static final String CHAVE = "chave";
    public static final String TIPO_CHAVE = "tipoChave";
    public static final String DATA = "data";

    public static Transaction apply(final Document document) {
        var transaction = new Transaction();
        transaction.setId(document.getString(ID));
        transaction.setValor(BigDecimal.valueOf(document.get(VALOR,
                Decimal128.class).doubleValue()));
        transaction.setStatus(StatusPix.valueOf(document.getString(STATUS)));
        transaction.setLinha(document.getString(LINHA));
        transaction.setChave(document.getString(CHAVE));
        transaction.setTipoChave(document.getString(TIPO_CHAVE));
        transaction.setData(document.get(DATA, Date.class).toInstant()
                .atZone(ZoneId.of(AMERICA_SAO_PAULO)).toLocalDateTime()
        );
        return transaction;
    }

}
