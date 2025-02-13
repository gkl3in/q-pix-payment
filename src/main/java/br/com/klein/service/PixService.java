package br.com.klein.service;

import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.UUID;

import br.com.klein.model.Chave;
import br.com.klein.model.LinhaDigitavel;
import br.com.klein.model.qrcode.DadosEnvio;
import br.com.klein.model.qrcode.QrCode;

@ApplicationScoped
public class PixService {

    private static final String QRCODE_PATH = "/tmp/qrcode";

    public LinhaDigitavel gerarLinhaDigitavel(final Chave chave, BigDecimal valor, String cidadeRemetente) {
        QrCode qrCode = new QrCode(new DadosEnvio(chave, valor, cidadeRemetente));
        String uuid = UUID.randomUUID().toString();
        String imagePath = QRCODE_PATH + uuid + ".png";
        final var imagePath2 = "qrcode.png";
        qrCode.save();

        String qrCodeString = qrCode.toString();
        return new LinhaDigitavel(qrCodeString, uuid);
    }
}