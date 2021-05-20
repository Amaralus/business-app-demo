package amaralus.apps.businesappdemo.infrastructure.audit.factory;

import amaralus.apps.businesappdemo.infrastructure.audit.metadata.EntityMetadata;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent.AuditLibraryEventBuilder;

import java.util.Map;

public abstract class AbstractEventFactory implements EventFactory {

    protected boolean success;
    protected String groupCode;
    protected String eventCode;
    protected Map<String, String> params;
    protected Object oldAuditEntity;
    protected Object newAuditEntity;
    protected EntityMetadata entityMetadata;

    protected AuditLibraryEventBuilder auditLibraryEventBuilder;

    public static EventFactory createEventFactory(FactoryType factoryType) {
        switch (factoryType) {
            case CREATE_ENTITY_FACTORY:
                return new CreateEntityEventFactory();
            case UPDATE_ENTITY_FACTORY:
                return new UpdateEntityEventFactory();
            case DELETE_ENTITY_FACTORY:
                return new DeleteEntityEventFactory();
            default:
                return new DefaultEventFactory();
        }
    }

    protected void createAuditLibraryEventBuilder() {
        auditLibraryEventBuilder = new AuditLibraryEventBuilder(groupCode, eventCode, success);
    }

    @Override
    public EventFactory success(boolean success) {
        this.success = success;
        return this;
    }

    @Override
    public EventFactory groupCode(String groupCode) {
        this.groupCode = groupCode;
        return this;
    }

    @Override
    public EventFactory eventCode(String eventCode) {
        this.eventCode = eventCode;
        return this;
    }

    @Override
    public EventFactory params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public EventFactory oldAuditEntity(Object oldAuditEntity) {
        this.oldAuditEntity = oldAuditEntity;
        return this;
    }

    @Override
    public EventFactory newAuditEntity(Object newAuditEntity) {
        this.newAuditEntity = newAuditEntity;
        return this;
    }

    @Override
    public EventFactory entityMetadata(EntityMetadata entityMetadata) {
        this.entityMetadata = entityMetadata;
        return this;
    }
}
