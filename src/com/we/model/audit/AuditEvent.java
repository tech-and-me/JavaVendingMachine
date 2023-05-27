package com.we.model.audit;

import java.time.LocalDateTime;

public class AuditEvent {
	private String eventDetails;
    private LocalDateTime timestamp;
    
    public AuditEvent(LocalDateTime timestamp,String eventDetails) {
        this.eventDetails = eventDetails;
        this.timestamp = timestamp;
    }


	public String getEventDetails() {
        return eventDetails;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }


	@Override
	public String toString() {
		return "AuditEvent [eventDetails=" + eventDetails + ", timestamp=" + timestamp + "]";
	}
    
    
}
