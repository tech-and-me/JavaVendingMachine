package com.we.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.we.model.audit.AuditEvent;

public class AuditDAO implements IAuditDAO {
	private List<AuditEvent> auditLog;
	
	public AuditDAO() {
        auditLog = new ArrayList<>();
    }
	
	@Override
	public void addAuditEvent(AuditEvent event) {
		auditLog.add(event);

	}

	@Override
	public List<AuditEvent> getAuditLog() {
		return new ArrayList<>(auditLog);
	}

	@Override
	public void logEvent(String message) {
		AuditEvent event = new AuditEvent(LocalDateTime.now(), message);
		addAuditEvent(event);
		
	}

	@Override
	public void logException(String logMessage) {
		String message = "Exception occurred:" + logMessage;
		AuditEvent event = new AuditEvent(LocalDateTime.now(),logMessage);
		addAuditEvent(event);
		
	}

}
