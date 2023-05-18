package com.defense.demo.service;

import com.defense.demo.model.Student;
import com.defense.demo.repository.StudentRepository;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PdfService {

    private final ITesseract tesseract;
    private final StudentRepository studentRepository;

    public PdfService(ITesseract tesseract, StudentRepository studentRepository) {
        this.tesseract = tesseract;
        this.studentRepository = studentRepository;
    }

    public Student findStudentByIdFromPdf(MultipartFile file) throws IOException {
        try {
            File tempFile = File.createTempFile("temp", ".pdf");
            file.transferTo(tempFile);

            String text = tesseract.doOCR(tempFile);

            tempFile.delete();

            String extractedId = extractSevenDigitCode(text);

            if (extractedId != null) {
                int id = Integer.parseInt(extractedId);
                return studentRepository.findById((id)).orElse(null);
            } else {
                return null;
            }
        } catch (IOException | TesseractException e) {
            throw new IOException("Failed to extract text from PDF.", e);
        }
    }

    private String extractSevenDigitCode(String text) {
        String regex = "\\b\\d{7}\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group();
        } else {
            return null; // Return null if no 7-digit code is found
        }
    }
}
