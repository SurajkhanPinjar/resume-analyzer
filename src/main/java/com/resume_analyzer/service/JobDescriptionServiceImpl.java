package com.resume_analyzer.service;

import com.resume_analyzer.dto.JobDescriptionParsedResponse;
import com.resume_analyzer.parser.JobDescriptionParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobDescriptionServiceImpl implements JobDescriptionService {

    private final JobDescriptionParser parser;

    @Override
    public JobDescriptionParsedResponse parse(String jdText) {
        return parser.parse(jdText);
    }
}