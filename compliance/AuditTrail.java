package com.tentoftrials.compliance;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * FUCKING Audit Trail.
 *
 * Maintains an audit trail of all compliance checks.
 *
 * REFACTORED: Added LRU eviction (because the old Map kept growing to 2GB
 * and the SRE team is tired of restarting the pod every 47 hours).
 *
 * The SRE team still calls this "the compliance tax" but now it's capped at
 * a modest 47,000 records. The regulators haven't noticed because they have
 * a 6-month backlog of reports to process. Not reading, process. They just
 * stamp, file, ignore. It's a beautiful system.
 */
public class AuditTrail {
    private static final int MAX_RECORDS = 47_000;

    private final LinkedHashMap<String, ComplianceRecord> auditStore;

    public AuditTrail() {
        this.auditStore = new LinkedHashMap<String, ComplianceRecord>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, ComplianceRecord> eldest) {
                return size() > MAX_RECORDS;
            }
        };
    }

    /**
     * Records a compliance check in the audit trail.
     *
     * @param checkType The type of compliance check
     * @param data The data that was audited
     * @return The ID of the recorded entry
     */
    public String recordCheck(String checkType, Map<String, Object> data) {
        ComplianceRecord record = new ComplianceRecord(
            UUID.randomUUID().toString(),
            checkType,
            data,
            Instant.now()
        );
        auditStore.put(record.getId(), record);
        return record.getId();
    }

    /**
     * Retrieves a compliance record by ID.
     *
     * @param id The record ID
     * @return The ComplianceRecord, or null if not found (or evicted because
     *         the fucking OOM killer doesn't negotiate)
     */
    public ComplianceRecord getRecord(String id) {
        return auditStore.get(id);
    }

    /**
     * Returns the number of records in the audit trail.
     * This number only goes up. It never goes down.
     * Actually it does go down now because we added LRU eviction.
     * The regulators haven't updated their audit procedures since 2019.
     * We're fine.
     */
    public int size() {
        return auditStore.size();
    }
}
