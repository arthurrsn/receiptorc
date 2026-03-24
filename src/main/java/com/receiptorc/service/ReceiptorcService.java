package com.receiptorc.service;

import com.receiptorc.dto.ReceiptResponseDTO;
import com.receiptorc.exceptions.TechnicalException;
import com.receiptorc.ports.IReceiptService;
import com.receiptorc.ports.IRequestToAi;
import com.receiptorc.ports.IRequestToOcr;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.regex.PatternSyntaxException;

@Service
public class ReceiptorcService implements IReceiptService {
    private final IRequestToAi requestToAi;
    private final IRequestToOcr requestToOcr;

    public ReceiptorcService(RequestToAi requestToAi, RequestToOcr requestToOcr){
        this.requestToAi = requestToAi;
        this.requestToOcr = requestToOcr;
    }

    /**
     * Orchestra all situations about image ocr
     * @param fileImage receives a receipt image to make ocr
     */
    public ReceiptResponseDTO receiptorc(File fileImage) {
        ObjectMapper mapper = new ObjectMapper();
        double finalValue = 0.0;

        String messageOcr = this.requestToOcr.requestToOcr(fileImage, mapper);
        String valueReceiptByAI = this.requestToAi.requestToAI(messageOcr, mapper);

        try {
            if (valueReceiptByAI != null && !valueReceiptByAI.equals("0.0")) {
                String cleanValue = valueReceiptByAI.replaceAll("[^0-9.]", "");
                finalValue = Double.parseDouble(cleanValue);
            }
        } catch (NumberFormatException _){
            throw new TechnicalException("Has an error with number format.");
        } catch (PatternSyntaxException _){
            throw new TechnicalException("The AI's return is broke. Has an error to clen and threat that.");
        }
        return new ReceiptResponseDTO(finalValue);
    }
}
