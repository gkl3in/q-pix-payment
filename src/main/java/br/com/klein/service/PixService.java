package br.com.klein.service;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import br.com.klein.model.Chave;
import br.com.klein.model.LinhaDigitavel;
import br.com.klein.model.qrcode.DadosEnvio;
import br.com.klein.model.qrcode.QrCode;
import io.smallrye.common.constraint.NotNull;

@ApplicationScoped
public class PixService {

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
    private static final String QR_CODE_PREFIX = "qrcode-pix-";
    private static final String QR_CODE_EXTENSION = ".png";

    public BufferedInputStream gerarQrCode(@NotNull final String uuid) throws IOException {
        //TODO depois criar tratamento de exceção global, e no lugar do IOException, utilizar QRCodeNotFoundException
        File qrCodeFile = findQrCodeFile(uuid)
            .orElseThrow(() -> new IOException("QR Code não encontrado para o UUID: " + uuid));

        return createBufferedInputStream(qrCodeFile);
    }

    public LinhaDigitavel gerarLinhaDigitavel(
            @NotNull final Chave chave, 
            @NotNull final BigDecimal valor, 
            @NotNull final String cidadeRemetente) {
        
        validateInputs(chave, valor, cidadeRemetente);
        
        String uuid = generateUUID();
        QrCode qrCode = createQrCode(chave, valor, cidadeRemetente);
        
        Path filePath = createTemporaryFile(uuid);
        qrCode.save(filePath);

        return new LinhaDigitavel(qrCode.toString(), uuid);
    }

    private Optional<File> findQrCodeFile(String uuid) {
        File tempDir = new File(TEMP_DIR);
        File[] matchingFiles = tempDir.listFiles((dir, name) -> 
            name.startsWith(QR_CODE_PREFIX) && name.contains(uuid));
        boolean hasMatchingFiles = matchingFiles != null && matchingFiles.length > 0;
        
        return hasMatchingFiles 
            ? Optional.of(matchingFiles[0]) 
            : Optional.empty();
    }

    private BufferedInputStream createBufferedInputStream(File file) throws IOException {
        try {
            return new BufferedInputStream(new FileInputStream(file));
        } catch (IOException e) {
            throw new IOException("Erro ao ler o arquivo QR code: " + file.getName(), e);
        }
    }

    private Path createTemporaryFile(String uuid) {
        try {
            return Path.of(File.createTempFile(QR_CODE_PREFIX + uuid, QR_CODE_EXTENSION)
                .getAbsoluteFile()
                .getPath());
        } catch (IOException e) {
            throw new UncheckedIOException("Erro ao criar arquivo temporário para QR code", e);
        }
    }

    private void validateInputs(Chave chave, BigDecimal valor, String cidadeRemetente) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }
        if (cidadeRemetente.trim().isEmpty()) {
            throw new IllegalArgumentException("Cidade do remetente não pode estar vazia");
        }
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    private QrCode createQrCode(Chave chave, BigDecimal valor, String cidadeRemetente) {
        return new QrCode(new DadosEnvio(chave, valor, cidadeRemetente));
    }
}