package amaralus.apps.businesappdemo.infrastructure.audit.factory;

import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent;

public class CreateEntityEventFactory extends EntityEventFactory {

    public CreateEntityEventFactory() {
        eventCode("create" + eventCode);
        createAuditLibraryEventBuilder();
    }

    @Override
    public AuditLibraryEvent produce() {
        return null;
    }
}
