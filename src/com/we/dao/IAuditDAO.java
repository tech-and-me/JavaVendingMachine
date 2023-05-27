package com.we.dao;

import java.util.List;

import com.we.model.audit.AuditEvent;

public interface IAuditDAO {
	void addAuditEvent(AuditEvent event);
	List<AuditEvent> getAuditLog();
	void logEvent(String message);
	void logException(String message);
}
