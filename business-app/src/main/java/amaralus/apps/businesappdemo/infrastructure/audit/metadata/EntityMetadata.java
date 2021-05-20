package amaralus.apps.businesappdemo.infrastructure.audit.metadata;

import amaralus.apps.businesappdemo.infrastructure.audit.AuditEntity;

import java.util.Collections;
import java.util.List;

public class EntityMetadata {

    private final Class<?> entityClass;
    private final String groupCode;
    private final List<FieldMetadata> fieldsMetadata;
    private final FieldMetadata idFieldMetadata;

    public EntityMetadata(Class<?> entityClass, List<FieldMetadata> fieldsMetadata) {
        this.entityClass = entityClass;
        this.fieldsMetadata = fieldsMetadata;
        this.groupCode = entityClass.getAnnotation(AuditEntity.class).groupCode();
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

    public List<FieldMetadata> getFieldsMetadata() {
        return Collections.unmodifiableList(fieldsMetadata);
    }

    public FieldMetadata getIdFieldMetadata() {
        return idFieldMetadata;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder("Entity metadata:").append("\n")
                .append("class: [").append(entityClass.getName()).append("]\n")
                .append("group code: [").append(groupCode).append("]\n")
                .append("fields metadata:\n");

        for (var fieldMetadata : fieldsMetadata)
            builder.append("    name: [").append(fieldMetadata.getName()).append("]\n")
                    .append("    field class: [").append(fieldMetadata.getFieldClass().getName()).append("]\n")
                    .append("    type: ").append(fieldMetadata.getType()).append("\n")
                    .append("    contains getter: ").append(fieldMetadata.getGetterMethod() != null).append("\n")
                    .append("    is id-field: ").append(fieldMetadata.isIdField()).append("\n");

        return builder.toString();
    }
}
