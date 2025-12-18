package com.resume_analyzer.service;

import com.resume_analyzer.dto.JobDescriptionParsedResponse;

public interface JobDescriptionService {
    JobDescriptionParsedResponse parse(String jdText);
}