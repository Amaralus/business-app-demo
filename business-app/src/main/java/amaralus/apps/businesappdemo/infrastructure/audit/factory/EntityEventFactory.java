package amaralus.apps.businesappdemo.infrastructure.audit.factory;

public abstract class EntityEventFactory extends AbstractEventFactory {

    public EntityEventFactory() {
        groupCode(entityMetadata.getGroupCode());
        eventCode(entityMetadata.getEntityClass().getSimpleName());
    }
}
