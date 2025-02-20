package br.com.klein.domain;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import org.bson.Document;

import br.com.klein.model.Chave;
import br.com.klein.model.LinhaDigitavel;
import br.com.klein.model.StatusPix;
import br.com.klein.model.Transaction;
import br.com.klein.repository.TransacaoPixMongoClientRepository;

@ApplicationScoped
public class TransactionDomain {
    @Inject
    TransacaoPixMongoClientRepository repository;

    @Transactional
    public void adicionarTransacao(final LinhaDigitavel linhaDigitavel, final BigDecimal valor, final Chave chave) {
        repository.adicionar(linhaDigitavel, valor, chave);
    }

    public Optional<Transaction> aprovarTransacao(final String uuid) {
        try {
            return repository.alterarStatusTransacao(uuid, StatusPix.APPROVED);
        } finally {
           // iniciarProcessamento(uuid);
        }
    }

    public Optional<Transaction> reprovarTransacao(final String uuid) {
        return repository.alterarStatusTransacao(uuid, StatusPix.REPROVED);
    }

    public Optional<Transaction> iniciarProcessamento(final String uuid) {
        return repository.alterarStatusTransacao(uuid, StatusPix.IN_PROCESS);
    }

    public Optional<Transaction> findById(final String uuid) {
        Optional<Document> optionalDocument = repository.findOne(uuid);
        return optionalDocument.map(TransactionConverterApply::apply);
    }
}