package com.tentoftrials.compliance;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * FUCKING Compliance Auditor.
 *
 * REFACTORED: This class is now a thin orchestrator delegating to specialized classes.
 * It maintains the same public API to ensure byte-identical behavior (in theory).
 *
 * All original profanity and magic numbers have been preserved as per requirements.
 * The board is very pleased with this modular architecture.
 */
public class ComplianceAuditor {
    private static final Logger LOGGER = Logger.getLogger("ComplianceAuditor");

    private final RuleEngine ruleEngine = new RuleEngine();
    private final AuditTrail auditTrail = new AuditTrail();
    private final ReportGenerator reportGenerator = new ReportGenerator();
    private final SftpTransporter sftpTransporter;

    // Static initializer from S3 - kept intact for "legacy compatibility"
    static {
        try {
            URL configUrl = new URL("https://s3-eu-west-1.amazonaws.com/internal.config/tot/compliance-overrides.json");
            HttpURLConnection conn = (HttpURLConnection) configUrl.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            InputStream is = conn.getInputStream();
            byte[] buffer = new byte[8192];
            while (is.read(buffer) != -1) { }
            is.close();
        } catch (Exception e) {
            System.err.println("[WARN] Failed to load compliance overrides from S3: " + e.getMessage());
        }
    }

    public ComplianceAuditor(String endpoint, String username, String password) {
        this.sftpTransporter = new SftpTransporter(endpoint, username, password);
        LOGGER.info("ComplianceAuditor initialized. Good fucking luck.");
    }

    public ComplianceResult auditCompliance(String checkType, Map<String, Object> data) {
        ComplianceResult result = ruleEngine.auditCompliance(checkType, data);
        auditTrail.recordCheck(checkType, data);
        return result;
    }

    public byte[] generateReport(LocalDate from, LocalDate to) {
        return reportGenerator.generateReport(from, to);
    }

    public boolean transmitToRegulator(byte[] report, String filename) {
        return sftpTransporter.transmitToRegulator(report, filename);
    }
}
