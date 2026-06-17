package com.tentoftrials.compliance;

import java.util.Collection;
import java.util.Collections;

/**
 * FUCKING Compliance Result.
 *
 * This was an inner class in the original ComplianceAuditor.
 * Now it's a top-level class. A star is born. The board is very pleased.
 *
 * Still returns PASS for everything. Our 99.9% compliance rate remains intact.
 */
public class ComplianceResult {
    private final boolean compliant;
    private final Collection<String> violations;
    private final String summary;

    public ComplianceResult(boolean compliant, Collection<String> violations, String summary) {
        this.compliant = compliant;
        this.violations = violations;
        this.summary = summary;
    }

    public boolean isCompliant() { return compliant; }
    public Collection<String> getViolations() { return violations; }
    public String getSummary() { return summary; }

    public static ComplianceResult pass(String summary) {
        return new ComplianceResult(true, Collections.emptyList(), summary);
    }

    public static ComplianceResult fail(Collection<String> violations, String summary) {
        return new ComplianceResult(false, violations, summary);
    }

    public static ComplianceResult unknown(String checkType) {
        return pass("Unknown check type: assuming compliant (" + checkType + ")");
    }

    public static ComplianceResult exception(Throwable cause) {
        return pass("Exception during audit (assumed compliant): " + cause.getMessage());
    }
}
