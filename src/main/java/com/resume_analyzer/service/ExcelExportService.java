package com.resume_analyzer.service;

import com.resume_analyzer.dto.CandidateScore;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ExcelExportService {

    public byte[] export(List<CandidateScore> results) {

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Ranked Candidates");

            // ===============================
            // Styles
            // ===============================

            // Header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Wrap text style
            CellStyle wrapStyle = workbook.createCellStyle();
            wrapStyle.setWrapText(true);
            wrapStyle.setVerticalAlignment(VerticalAlignment.TOP);

            // Score color styles
            CellStyle greenStyle = workbook.createCellStyle();
            greenStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            greenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle yellowStyle = workbook.createCellStyle();
            yellowStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            yellowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle redStyle = workbook.createCellStyle();
            redStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
            redStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // ===============================
            // Header
            // ===============================

            String[] cols = {
                    "Rank",
                    "Candidate ID",
                    "Name",
                    "Score",
                    "Confidence",
                    "Decision",
                    "Strengths",
                    "Gaps",
                    "Recommendation"
            };

            Row header = sheet.createRow(0);
            for (int i = 0; i < cols.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(cols[i]);
                cell.setCellStyle(headerStyle);
            }

            // ===============================
            // Data Rows
            // ===============================

            int rowIdx = 1;
            int rank = 1;

            for (CandidateScore cs : results) {

                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(rank++);
                row.createCell(1).setCellValue(cs.getCandidateId());
                row.createCell(2).setCellValue(cs.getName());

                // Score + decision logic
                Cell scoreCell = row.createCell(3);
                scoreCell.setCellValue(cs.getOverallScore());

                String decision;
                if (cs.getOverallScore() >= 85) {
                    scoreCell.setCellStyle(greenStyle);
                    decision = "Strong Hire";
                } else if (cs.getOverallScore() >= 70) {
                    scoreCell.setCellStyle(yellowStyle);
                    decision = "Consider";
                } else {
                    scoreCell.setCellStyle(redStyle);
                    decision = "Reject";
                }

                row.createCell(4).setCellValue(cs.getConfidence());
                row.createCell(5).setCellValue(decision);

                // Strengths
                Cell strengthsCell = row.createCell(6);
                strengthsCell.setCellValue(String.join(", ", cs.getStrengths()));
                strengthsCell.setCellStyle(wrapStyle);

                // Gaps
                Cell gapsCell = row.createCell(7);
                gapsCell.setCellValue(String.join(", ", cs.getGaps()));
                gapsCell.setCellStyle(wrapStyle);

                // Recommendation
                Cell recCell = row.createCell(8);
                recCell.setCellValue(cs.getRecommendation());
                recCell.setCellStyle(wrapStyle);
            }

            // ===============================
            // Auto-size columns
            // ===============================
            for (int i = 0; i < cols.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Excel", e);
        }
    }
}