package com.resume_analyzer.entity;

public enum SubscriptionPlan {

    STARTER(100, 10),
    ADVANCED(250, 25),
    UNLIMITED(Integer.MAX_VALUE, Integer.MAX_VALUE);

    private final int resumeLimit;
    private final int zipLimit;

    SubscriptionPlan(int resumeLimit, int zipLimit) {
        this.resumeLimit = resumeLimit;
        this.zipLimit = zipLimit;
    }

    public int getResumeLimit() {
        return resumeLimit;
    }

    public int getZipLimit() {
        return zipLimit;
    }
}