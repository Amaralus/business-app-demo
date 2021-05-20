package amaralus.apps.businesappdemo.infrastructure.audit.metadata;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
public class FieldMetadata {

    private final Class<?> entityClass;
    private final String name;
    private final Class<?> fieldClass;
    private final FieldMetadataType type;
    private final Method getterMethod;
    private final boolean idField;

    public FieldMetadata(Class<?> entityClass, String name, Class<?> fieldClass, FieldMetadataType type, Method getterMethod, boolean idField) {
        this.entityClass = entityClass;
        this.name = name;
        this.fieldClass = fieldClass;
        this.type = type;
        this.getterMethod = getterMethod;
        this.idField = idField;
    }

    public Object extractData(Object targetObject) {
        try {
            if (getterMethod != null)
                return getterMethod.invoke(targetObject);
            else {
                var field = entityClass.getDeclaredField(name);
                field.setAccessible(true);
                return field.get(targetObject);
            }
        } catch (Exception e) {
            log.warn("Unsuccessful data extraction from field [" + name + "]");
            return "DATA EXTRACTION ERROR";
        }
    }

    public String getName() {
        return name;
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
}
