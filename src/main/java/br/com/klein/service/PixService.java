package br.com.klein.service;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import br.com.klein.model.Chave;
import br.com.klein.model.LinhaDigitavel;
import br.com.klein.model.qrcode.DadosEnvio;
import br.com.klein.model.qrcode.QrCode;

@ApplicationScoped
public class PixService {

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    public BufferedInputStream gerarQrCode(final String uuid) throws IOException {
        File tempDir = new File(TEMP_DIR);
        File[] matchingFiles = tempDir.listFiles((dir, name) -> name.contains(uuid));
        
        if (matchingFiles == null || matchingFiles.length == 0) {
            throw new IOException("QR Code file not found for UUID: " + uuid);
        }

        return new BufferedInputStream(new FileInputStream(matchingFiles[0]));
    }

    public LinhaDigitavel gerarLinhaDigitavel(final Chave chave, BigDecimal valor, String cidadeRemetente) {
        QrCode qrCode = new QrCode(new DadosEnvio(chave, valor, cidadeRemetente));
        String uuid = UUID.randomUUID().toString();

        qrCode.save(tempImgFilePath(uuid));

        String qrCodeString = qrCode.toString();
        return new LinhaDigitavel(qrCodeString, uuid);
    }

    private static Path tempImgFilePath(final String uuid) {
        try {
            return Path.of(File.createTempFile("qrcode-pix-" + uuid, ".png").getAbsoluteFile().getPath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}