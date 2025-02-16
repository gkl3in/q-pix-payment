package br.com.klein.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.com.klein.config.RedisCache;
import br.com.klein.model.Chave;
import br.com.klein.model.TipoChave;
import br.com.klein.model.TipoPessoa;
import io.quarkus.logging.Log;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class DictService {

    @ConfigProperty(name = "pix.chave")
    private String chave;
    @ConfigProperty(name = "pix.ispb")
    private String ispb;
    @ConfigProperty(name = "pix.cnpj")
    private String cnpj;
    @ConfigProperty(name = "pix.nome")
    private String nome;

    @Inject
    RedisCache redisCache;

    public Chave buscarDetalhesChave(String key) {
        Chave chave = buscarChaveCache(key);

        if (Objects.isNull(chave)) {
            var chaveFake = buscarChave(key);
            redisCache.set(key, buscarChave(key));
            return chaveFake;
        }

        return chave;
    }

    private Chave buscarChaveCache(String key) {
        Optional<Chave> optionalChave = redisCache.get(key);

        if (optionalChave.isPresent()) {
            Chave chave = optionalChave.get();
            Log.infof("Chave encontrada no cache %s", chave);
            return chave;
        }
        Log.info("Chave não encontrada no cache");
        return null;
    }

    public Chave buscarChave(String chave) {
        return new Chave(TipoChave.CPF,
                chave,
                ispb,
                TipoPessoa.FISICA,
                cnpj,
                nome,
                LocalDateTime.now());
    }
}
