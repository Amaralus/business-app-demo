package amaralus.apps.businesappdemo.infrastructure.audit.factory;

import amaralus.apps.businesappdemo.infrastructure.audit.metadata.EntityMetadata;

import java.util.Map;
import java.util.NoSuchElementException;

public abstract class AbstractEventFactory implements EventFactory {

    protected boolean success;
    protected String eventGroup;
    protected String eventCode;
    protected Map<String, String> params;
    protected Object oldAuditEntity;
    protected Object newAuditEntity;
    protected EntityMetadata entityMetadata;

    public static EventFactory createEventFactory(FactoryType factoryType) {
        switch (factoryType) {
            case CREATE_ENTITY_FACTORY:
                return new CreateEntityEventFactory();
            case UPDATE_ENTITY_FACTORY:
                return new UpdateEntityEventFactory();
            case DELETE_ENTITY_FACTORY:
                return new DeleteEntityEventFactory();
            default:
                throw new NoSuchElementException("factory for type " + factoryType + " doesn't exist");
        }
    }

    @Override
    public EventFactory success(boolean success) {
        this.success = success;
        return this;
    }

    @Override
    public EventFactory eventGroup(String eventGroup) {
        this.eventGroup = eventGroup;
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
