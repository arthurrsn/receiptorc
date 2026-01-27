package com.receiptorc.service;

import com.receiptorc.exceptions.TechnicalException;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;

@Service
public class ReceiptValidation implements IReceiptValidation {
    private final Tika tika;

    public ReceiptValidation(Tika tika) {
        this.tika = tika;
    }

    private static final Set<String> EXTENSIONS_ALLOWED = Set.of(
            "image/png", "image/jpeg", "image/webp");

    /**
     * This function valid the file param to ensure that it is not null or don't be a type allowed by system
     * @param file is the file uploaded by user
     * @throws TechnicalException will catch any problem with extension of file
     * @throws IOException if the file has any problem unknow
     */
    @Override
    public void validateController(MultipartFile file) throws TechnicalException, IOException {
        if (file.isEmpty()) {
            throw new TechnicalException("The file is empty.");
        }

        // Verify by Magic Bytes
        String mimeType;
        try (var is = file.getInputStream()) {
            mimeType = this.tika.detect(is);
        }

        if (!EXTENSIONS_ALLOWED.contains(mimeType)) {
            throw new TechnicalException("The file type " + mimeType + " is not allowed.");
        }

        // structure Verify (ImageIO)
        try (var is = file.getInputStream()) {
            BufferedImage image = ImageIO.read(is);
            if (image == null) {
                // if Tika detect type but the ImageIo returns null,
                // the file has been a "false positive" malicious.
                // This often happens when manually changing the file extension.
                throw new TechnicalException("Security Alert: File signature matches " + mimeType + " but content is unreadable.");
            }
        } catch (IOException e) {
            throw new TechnicalException("Error to process image: ", e);
        }
    }
}
