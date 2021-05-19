package amaralus.apps.businesappdemo.infrastructure.audit.metadata;

public class EntityMetadata {

    private final Class<?> entityClass;

    public EntityMetadata(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }
}
