package amaralus.apps.businesappdemo.infrastructure.audit.metadata;

import amaralus.apps.businesappdemo.infrastructure.audit.AuditEntity;

import java.util.Collections;
import java.util.List;

public class EntityMetadata {

    private final Class<?> entityClass;
    private final String groupCode;
    private final List<FieldMetadata> fieldsMetadata;

    public EntityMetadata(Class<?> entityClass, List<FieldMetadata> fieldsMetadata) {
        this.entityClass = entityClass;
        this.fieldsMetadata = fieldsMetadata;
        this.groupCode = entityClass.getAnnotation(AuditEntity.class).groupCode();

    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public List<FieldMetadata> getFieldsMetadata() {
        return Collections.unmodifiableList(fieldsMetadata);
    }
}
