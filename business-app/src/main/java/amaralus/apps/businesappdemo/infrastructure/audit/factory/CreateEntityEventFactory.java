package amaralus.apps.businesappdemo.infrastructure.audit.factory;

import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent;

public class CreateEntityEventFactory extends EntityEventFactory {

    @Override
    public AuditLibraryEvent produce() {
        groupCode(entityMetadata.getGroupCode());
        eventCode("create" + entityMetadata.getEntityClass().getSimpleName());

        return null;
    }
}
