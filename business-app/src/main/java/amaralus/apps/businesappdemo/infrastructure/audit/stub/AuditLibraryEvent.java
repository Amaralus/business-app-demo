package amaralus.apps.businesappdemo.infrastructure.audit.stub;

import java.util.HashMap;
import java.util.Map;

public class AuditLibraryEvent {

    private String groupCode;
    private String eventCode;
    private String auditContextUUID;
    private boolean success;

    private final Map<String, String> params = new HashMap<>();

    @Override
    public String toString() {
        var builder = new StringBuilder("Audit event:\n")
                .append("groupCode: ").append(groupCode).append("\n")
                .append("eventCode: ").append(eventCode).append("\n")
                .append("success: ").append(success).append("\n")
                .append("auditContextUUID: ").append(auditContextUUID).append("\n")
                .append("params:\n");

        params.forEach((name, value) -> builder.append("    ").append(name).append(": ").append(value).append("\n"));

        return builder.toString();
    }

    public static class AuditLibraryEventBuilder {
        private final AuditLibraryEvent event = new AuditLibraryEvent();

        public AuditLibraryEventBuilder(String groupCode, String eventCode, boolean success) {
            this.event.groupCode = groupCode;
            this.event.eventCode = eventCode;
            this.event.success = success;
        }

        public AuditLibraryEventBuilder param(String name, Object value) {
            event.params.put(name, value.toString());
            return this;
        }

        public AuditLibraryEventBuilder fillAuditContextUUID(String auditContextUUID) {
            event.auditContextUUID = auditContextUUID;
            return this;
        }

        public AuditLibraryEvent build() {
            return event;
        }
    }
}
