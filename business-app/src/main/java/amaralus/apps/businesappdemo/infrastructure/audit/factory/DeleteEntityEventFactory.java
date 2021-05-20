package amaralus.apps.businesappdemo.infrastructure.audit.factory;

import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent;

public class DeleteEntityEventFactory extends EntityEventFactory {

    public DeleteEntityEventFactory() {
        eventCode("delete" + eventCode);
        createAuditLibraryEventBuilder();
    }

    @Override
    public AuditLibraryEvent produce() {
        return null;
    }
}
