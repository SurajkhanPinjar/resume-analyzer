package com.resume_analyzer.service;

import com.resume_analyzer.entity.User;
import com.resume_analyzer.entity.Usage;
import org.springframework.stereotype.Service;

@Service
public class UsageValidationService {

    public void validateResumeUpload(User user, int resumeCount) {

        Usage usage = user.getUsage();

        if (usage.getResumeUsed() + resumeCount >
                user.getPlan().getResumeLimit()) {

            throw new RuntimeException(
                    "Resume limit exceeded for plan: " + user.getPlan()
            );
        }
    }

    public void validateZipUpload(User user) {

        Usage usage = user.getUsage();

        if (usage.getZipUsed() + 1 >
                user.getPlan().getZipLimit()) {

            throw new RuntimeException(
                    "ZIP upload limit exceeded for plan: " + user.getPlan()
            );
        }
    }

    public void incrementResume(User user, int count) {
        user.getUsage().setResumeUsed(
                user.getUsage().getResumeUsed() + count
        );
    }

    public void incrementZip(User user) {
        user.getUsage().setZipUsed(
                user.getUsage().getZipUsed() + 1
        );
    }
}