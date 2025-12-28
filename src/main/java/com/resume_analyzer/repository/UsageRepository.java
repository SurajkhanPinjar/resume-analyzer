package com.resume_analyzer.repository;

import com.resume_analyzer.entity.Usage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsageRepository extends JpaRepository<Usage, UUID> {
}