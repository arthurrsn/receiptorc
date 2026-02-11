package com.receiptorc.utils;

import com.receiptorc.exceptions.TechnicalException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Objects;

@Component
public class MultipartToFile {
    /**
     * Utility method to converts a multpart to a file.
     * @param multipartFile the file before convert
     * @return a type File archive
     */
    public static File multipartToFile(MultipartFile multipartFile) {
        File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (InputStream inputStream = multipartFile.getInputStream();
             OutputStream outputStream = new FileOutputStream(convFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new TechnicalException("MultipartFile cannot convert to File.", e);
        }
        return convFile;
    }
}
