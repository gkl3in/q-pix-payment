package br.com.klein.repository;

import org.bson.Document;

import br.com.klein.model.Chave;
import br.com.klein.model.LinhaDigitavel;
import br.com.klein.model.StatusPix;
import br.com.klein.model.Transaction;

import java.math.BigDecimal;
import java.util.Optional;

public interface TransactionRepository {
    void adicionar(final LinhaDigitavel linhaDigitavel, final BigDecimal valor, final Chave chave);
    Optional<Transaction> alterarStatusTransacao(final String uuid, final StatusPix statusPix);
    Optional<Document> findOne(final String uuid);
}
