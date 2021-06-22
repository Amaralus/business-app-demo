package amaralus.apps.businesappdemo.infrastructure.audit.metadata;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
public class FieldMetadata {

    private final Class<?> entityClass;
    private final String name;
    private final String paramName;
    private final Class<?> fieldClass;
    private final FieldMetadataType type;
    private final Method getterMethod;
    private final boolean idField;

    private boolean mandatory;
    private EntityMetadata entityMetadataLink;
    private boolean mapKeyToStringMode;

    public FieldMetadata(Class<?> entityClass, String name, String paramName, Class<?> fieldClass, FieldMetadataType type, Method getterMethod, boolean idField) {
        this.entityClass = entityClass;
        this.name = name;
        this.paramName = paramName;
        this.fieldClass = fieldClass;
        this.type = type;
        this.getterMethod = getterMethod;
        this.idField = idField;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public String getName() {
        return name;
    }

    public String getParamName() {
        return paramName;
    }

    public Class<?> getFieldClass() {
        return fieldClass;
    }

    public FieldMetadataType getType() {
        return type;
    }

    public Method getGetterMethod() {
        return getterMethod;
    }

    public boolean isIdField() {
        return idField;
    }

    public void setEntityMetadataLink(EntityMetadata entityMetadataLink) {
        this.entityMetadataLink = entityMetadataLink;
    }

    public EntityMetadata getEntityMetadataLink() {
        return entityMetadataLink;
    }

    public boolean isMapKeyToStringMode() {
        return mapKeyToStringMode;
    }

    public void setMapKeyToStringMode(boolean mapKeyToStringMode) {
        this.mapKeyToStringMode = mapKeyToStringMode;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }
}
