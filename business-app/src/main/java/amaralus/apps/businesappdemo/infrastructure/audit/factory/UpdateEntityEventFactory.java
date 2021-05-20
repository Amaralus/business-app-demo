package amaralus.apps.businesappdemo.infrastructure.audit.factory;

import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent;

public class UpdateEntityEventFactory extends EntityEventFactory {

    public UpdateEntityEventFactory() {
        eventCode("update" + eventCode);
        createAuditLibraryEventBuilder();
    }

    @Override
    public AuditLibraryEvent produce() {
        return null;
    }
}
