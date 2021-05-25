package amaralus.apps.businesappdemo.infrastructure.audit.factory;

import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent;

public class DeleteEntityEventFactory implements EventFactory {

    @Override
    public AuditLibraryEvent produce(EventData eventData) {
        var auditLibraryEventBuilder = new AuditLibraryEvent.AuditLibraryEventBuilder(
                eventData.getEntityMetadata().getGroupCode(),
                "delete" + eventData.getEntityMetadata().getEntityClass().getSimpleName(),
                eventData.isSuccess());

        var idField = eventData.getEntityMetadata().getIdFieldMetadata();
        var fieldValue = idField.extractData(eventData.getNewAuditEntity());

        auditLibraryEventBuilder.param(idField.getName(), fieldValue);

        return auditLibraryEventBuilder.build();
    }

    @Override
    public Type getFactoryType() {
        return Type.DELETE_ENTITY_FACTORY;
    }
}
