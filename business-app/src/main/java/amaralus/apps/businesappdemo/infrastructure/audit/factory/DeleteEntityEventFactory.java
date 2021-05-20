package amaralus.apps.businesappdemo.infrastructure.audit.factory;

import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent;

public class DeleteEntityEventFactory extends EntityEventFactory {

    @Override
    public AuditLibraryEvent produce() {
        groupCode(entityMetadata.getGroupCode());
        eventCode("delete" + entityMetadata.getEntityClass().getSimpleName());
        createAuditLibraryEventBuilder();

        var idField = entityMetadata.getIdFieldMetadata();
        var fieldValue = idField.extractData(newAuditEntity);

        auditLibraryEventBuilder.param(idField.getName(), fieldValue);

        return auditLibraryEventBuilder.build();
    }
}
