package amaralus.apps.businesappdemo.infrastructure.audit.factory;

import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent;

public class DefaultEventFactory extends AbstractEventFactory {

    public DefaultEventFactory() {
        createAuditLibraryEventBuilder();
    }

    @Override
    public AuditLibraryEvent produce() {
        params.forEach((name, value) -> auditLibraryEventBuilder.param(name, value));
        return auditLibraryEventBuilder.build();
    }
}
