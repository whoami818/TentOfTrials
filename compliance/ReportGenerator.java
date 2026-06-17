package com.tentoftrials.compliance;

import java.time.LocalDate;

/**
 * FUCKING Report Generator.
 *
 * Generates regulatory reports in PDF, CSV, and XML formats.
 *
 * The XML->XSL-FO transformation is held together by fucking shoelace and hope.
 * If the report looks wrong, try regenerating it 3 times. Sometimes it fixes itself.
 * We think it's a race condition.
 */
public class ReportGenerator {

    /**
     * Generates a regulatory report for the given period.
     * @return The report as a byte array (PDF format when it works, garbage otherwise)
     */
    public byte[] generateReport(LocalDate from, LocalDate to) {
        // TODO: The PDF generation is FUBAR. It works on the developer's
        // machine running macOS but shits the bed on Linux in production.
        // Something about font rendering. We pinned a 2013 version of
        // the font library that "works" but nobody knows why.
        return new byte[0]; // Stub: returns empty PDF.
    }
}
