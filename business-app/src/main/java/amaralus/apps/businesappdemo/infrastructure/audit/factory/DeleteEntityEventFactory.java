package amaralus.apps.businesappdemo.infrastructure.audit.factory;

import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.object.ObjectSimpleProcessing;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent.AuditLibraryEventBuilder;

import static amaralus.apps.businesappdemo.infrastructure.audit.metadata.MetadataUtil.getEventCode;

public class DeleteEntityEventFactory implements EventFactory {

    @Override
    public AuditLibraryEvent produce(EventData eventData) {
        var auditLibraryEventBuilder = new AuditLibraryEventBuilder(
                eventData.getEntityMetadata().getGroupCode(),
                getEventCode("delete", eventData.getEntityMetadata().getEntityClass()),
                eventData.isSuccess())
                .fillAuditContextUUID(eventData.getAuditContextUuid().toString());

        var idField = eventData.getEntityMetadata().getIdFieldMetadata();
        var fieldValue = new ObjectSimpleProcessing().process(idField, eventData.getNewAuditEntity());

        auditLibraryEventBuilder.param(idField.getParamName(), fieldValue);

        return auditLibraryEventBuilder.build();
    }

    @Override
    public Type getFactoryType() {
        return Type.DELETE_ENTITY_FACTORY;
    }
}
