package amaralus.apps.businesappdemo.infrastructure.audit.metadata;

import java.util.List;

public class EntityMetadata {

    private final Class<?> entityClass;
    private final List<FieldMetadata> fieldsMetadata;

    public EntityMetadata(Class<?> entityClass, List<FieldMetadata> fieldsMetadata) {
        this.entityClass = entityClass;
        this.fieldsMetadata = fieldsMetadata;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public List<FieldMetadata> getFieldsMetadata() {
        return fieldsMetadata;
    }
}
