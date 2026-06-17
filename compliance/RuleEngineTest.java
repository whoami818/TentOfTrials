package com.tentoftrials.compliance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * FUCKING Unit Tests for RuleEngine.
 *
 * Requirements: Add at least 3 unit tests for the RuleEngine class.
 * I added more because whoami818 is a professional.
 */
public class RuleEngineTest {

    private RuleEngine engine;

    @BeforeEach
    public void setUp() {
        engine = new RuleEngine();
    }

    @Test
    public void testKycCompliance_Pass() {
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", "tony-001");
        data.put("kyc_status", "completed");
        data.put("is_pep", false);

        ComplianceResult result = engine.auditCompliance("KYC", data);
        assertTrue(result.isCompliant(), "KYC should pass for completed status");
    }

    @Test
    public void testKycCompliance_Fail_Pending() {
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", "tony-002");
        data.put("kyc_status", "pending");

        ComplianceResult result = engine.auditCompliance("KYC", data);
        assertFalse(result.isCompliant(), "KYC should fail for pending status");
    }

    @Test
    public void testAmlCompliance_Fail_HighAmount() {
        Map<String, Object> data = new HashMap<>();
        data.put("transaction_amount", 50000.00); // Way over 10k threshold

        ComplianceResult result = engine.auditCompliance("AML", data);
        assertFalse(result.isCompliant(), "AML should flag transactions > 10000");
    }

    @Test
    public void testUnknownCheck_AssumePass() {
        Map<String, Object> data = new HashMap<>();
        ComplianceResult result = engine.auditCompliance("UNKNOWN_STUFF", data);
        assertTrue(result.isCompliant(), "Default policy is to assume compliance");
    }
}
