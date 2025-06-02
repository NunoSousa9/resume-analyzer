package com.resume.analyzer.controller;

import com.resume.analyzer.dto.ResumeAnalysisResponseDto;
import com.resume.analyzer.dto.ResumeRequestDto;
import com.resume.analyzer.model.Resume;
import com.resume.analyzer.repository.ResumeRepository;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;


@RestController
@RequestMapping("/api")
public class ResumeController {

    private final ResumeRepository resumeRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${analyzer.api.url}")
    private String analyzerApiUrl;

    public ResumeController(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    @PostMapping("/upload")
    public String uploadResume(@RequestBody ResumeRequestDto request) {
        Resume resume = new Resume();
        resume.setContent(request.getContent());
        resumeRepository.save(resume);
        return "Resume uploaded successfully";
    }

     @PostMapping("/analyze")
    public ResponseEntity<ResumeAnalysisResponseDto> analyzeResume(@RequestBody ResumeRequestDto request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ResumeRequestDto> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ResumeAnalysisResponseDto> response = restTemplate.postForEntity(
                analyzerApiUrl,
                entity,
                ResumeAnalysisResponseDto.class
        );

        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/upload-pdf")
    public ResponseEntity<ResumeAnalysisResponseDto> uploadPdf(@RequestParam("file") MultipartFile file) {
        try {
            // 1. Extrai texto do PDF
            PDDocument document = PDDocument.load(file.getInputStream());
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            document.close();

            // 2. Guarda no banco (opcional)
            Resume resume = new Resume();
            resume.setContent(text);
            resumeRepository.save(resume);

            // 3. Envia para o microserviço Python
            ResumeRequestDto requestDto = new ResumeRequestDto();
            requestDto.setContent(text);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ResumeRequestDto> entity = new HttpEntity<>(requestDto, headers);

            ResponseEntity<ResumeAnalysisResponseDto> response = restTemplate.postForEntity(
                    analyzerApiUrl,
                    entity,
                    ResumeAnalysisResponseDto.class
            );

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}