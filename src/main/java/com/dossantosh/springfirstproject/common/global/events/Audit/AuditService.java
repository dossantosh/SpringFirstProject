package com.dossantosh.springfirstproject.common.global.events.Audit;

import java.util.Map;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private final AuditEventRepository auditEventRepository;

    public AuditService(AuditEventRepository auditEventRepository) {
        this.auditEventRepository = auditEventRepository;
    }

    public void logCustomEvent(String principal, String type, Map<String, Object> data) {
        AuditEvent event = new AuditEvent(principal, type, data);
        auditEventRepository.add(event);
    }
}
