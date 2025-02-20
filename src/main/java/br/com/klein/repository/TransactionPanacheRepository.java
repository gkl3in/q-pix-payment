package br.com.klein.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import br.com.klein.domain.TransactionConverterApply;
import br.com.klein.model.Chave;
import br.com.klein.model.LinhaDigitavel;
import br.com.klein.model.StatusPix;
import br.com.klein.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static br.com.klein.repository.TransacaoPixMongoClientRepository.AMERICA_SAO_PAULO;

@ApplicationScoped
public class TransactionPanacheRepository implements PanacheMongoRepository<Transaction> {
    public void adicionar(LinhaDigitavel linhaDigitavel, BigDecimal valor, Chave chave) {
        var transaction = new Transaction();

        transaction.setChave(chave.chave());
        transaction.setData(LocalDateTime.now(ZoneId.of(AMERICA_SAO_PAULO)));
        transaction.setId(linhaDigitavel.uuid());
        transaction.setStatus(StatusPix.CREATED);
        transaction.setValor(valor);
        transaction.setLinha(linhaDigitavel.linha());
        transaction.setTipoChave(chave.tipoChave().toString());
        transaction.update();
    }

    public Optional<Transaction> alterarStatusTransacao(String uuid, StatusPix statusPix) {
        Optional<Transaction> optionalTransaction = findOne(uuid);

        if (optionalTransaction.isPresent()) {
            var transaction = optionalTransaction.get();
            transaction.setStatus(statusPix);
            transaction.update();

            return Optional.of(transaction);
        }
        return Optional.empty();
    }

    public Optional<Transaction> findOne(String uuid) {
        return find(TransactionConverterApply.ID, uuid).stream().findFirst();
    }
}
