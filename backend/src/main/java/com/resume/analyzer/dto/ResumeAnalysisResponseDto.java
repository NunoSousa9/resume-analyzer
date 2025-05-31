package com.resume.analyzer.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResumeAnalysisResponseDto {

    @JsonProperty("hard_skills")
    private List<String> hardSkills;

    @JsonProperty("soft_skills")
    private List<String> softSkills;

    private String summary;
    
    public List<String> getHardSkills() {
        return hardSkills;
    }
    public void setHardSkills(List<String> hardSkills) {
        this.hardSkills = hardSkills;
    }
    public List<String> getSoftSkills() {
        return softSkills;
    }
    public void setSoftSkills(List<String> softSkills) {
        this.softSkills = softSkills;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    
}
