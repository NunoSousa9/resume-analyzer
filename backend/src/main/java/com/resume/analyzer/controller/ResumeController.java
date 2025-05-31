package com.resume.analyzer.controller;

import com.resume.analyzer.dto.ResumeAnalysisResponseDto;
import com.resume.analyzer.dto.ResumeRequestDto;
import com.resume.analyzer.model.Resume;
import com.resume.analyzer.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
}