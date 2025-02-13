package br.com.klein.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.com.klein.model.Chave;
import br.com.klein.model.TipoChave;
import br.com.klein.model.TipoPessoa;

import java.time.LocalDateTime;

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
