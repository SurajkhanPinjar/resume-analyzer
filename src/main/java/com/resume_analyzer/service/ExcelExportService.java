package com.resume_analyzer.service;

import com.resume_analyzer.dto.CandidateScore;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ExcelExportService {

    public byte[] export(List<CandidateScore> results) {

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Ranked Candidates");

            Row header = sheet.createRow(0);
            String[] cols = {
                    "Rank", "Candidate ID", "Name",
                    "Score", "Confidence",
                    "Strengths", "Gaps"
            };

            for (int i = 0; i < cols.length; i++) {
                header.createCell(i).setCellValue(cols[i]);
            }

            int rowIdx = 1;
            int rank = 1;

            for (CandidateScore cs : results) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(rank++);
                row.createCell(1).setCellValue(cs.getCandidateId());
                row.createCell(2).setCellValue(cs.getName());
                row.createCell(3).setCellValue(cs.getOverallScore());
                row.createCell(4).setCellValue(cs.getConfidence());
                row.createCell(5).setCellValue(String.join(", ", cs.getStrengths()));
                row.createCell(6).setCellValue(String.join(", ", cs.getGaps()));
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Excel", e);
        }
    }
}