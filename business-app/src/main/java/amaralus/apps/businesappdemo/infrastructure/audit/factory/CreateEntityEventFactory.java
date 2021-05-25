package amaralus.apps.businesappdemo.infrastructure.audit.factory;

import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent;

public class CreateEntityEventFactory implements EventFactory {

    @Override
    public AuditLibraryEvent produce(EventData eventData) {
        var auditLibraryEventBuilder = new AuditLibraryEvent.AuditLibraryEventBuilder(
                eventData.getEntityMetadata().getGroupCode(),
                "create" + eventData.getEntityMetadata().getEntityClass().getSimpleName(),
                eventData.isSuccess());

        return auditLibraryEventBuilder.build();
    }

    @Override
    public Type getFactoryType() {
        return Type.CREATE_ENTITY_FACTORY;
    }
}
