package br.com.klein.domain;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import br.com.klein.model.Chave;
import br.com.klein.model.LinhaDigitavel;
import br.com.klein.model.StatusPix;
import br.com.klein.model.Transaction;
import br.com.klein.repository.TransactionPanacheRepository;

@ApplicationScoped
public class TransactionDomain {
    @Inject
    TransactionPanacheRepository repository;

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
        return repository.findOne(uuid);
    }

    public List<Transaction> buscarTransacoes(final Date dataInicio, final Date dataFim) {
        return repository.buscarTransacoes(dataInicio, dataFim);
    }
}