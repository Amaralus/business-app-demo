package amaralus.apps.businesappdemo.infrastructure.audit.factory;

import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent;

public class UpdateEntityEventFactory extends EntityEventFactory {

    @Override
    public AuditLibraryEvent produce() {
        groupCode(entityMetadata.getGroupCode());
        eventCode("update" + entityMetadata.getEntityClass().getSimpleName());

        return null;
    }
}
