package amaralus.apps.businesappdemo.infrastructure.audit.metadata;

import amaralus.apps.businesappdemo.infrastructure.audit.AuditEntity;

import java.util.Collections;
import java.util.List;

public class EntityMetadata {

    private final Class<?> entityClass;
    private final String groupCode;
    private final int walkDepth;
    private final List<FieldMetadata> fieldsMetadata;
    private final FieldMetadata idFieldMetadata;

    public EntityMetadata(Class<?> entityClass, List<FieldMetadata> fieldsMetadata) {
        this.entityClass = entityClass;
        this.fieldsMetadata = fieldsMetadata;

        var annotation = entityClass.getAnnotation(AuditEntity.class);
        this.groupCode = annotation.groupCode();
        this.walkDepth = annotation.walkDepth();
        this.idFieldMetadata = fieldsMetadata.stream()
                .filter(FieldMetadata::isIdField)
                .findFirst()
                .orElse(null);
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public int getWalkDepth() {
        return walkDepth;
    }

    public List<FieldMetadata> getFieldsMetadata() {
        return Collections.unmodifiableList(fieldsMetadata);
    }

    public FieldMetadata getIdFieldMetadata() {
        return idFieldMetadata;
    }
}
