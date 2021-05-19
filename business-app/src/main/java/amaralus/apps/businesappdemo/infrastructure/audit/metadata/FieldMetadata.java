package amaralus.apps.businesappdemo.infrastructure.audit.metadata;

import java.lang.reflect.Method;

public class FieldMetadata {

    private final String name;
    private final Class<?> type;
    private final Method getterMethod;

    public FieldMetadata(String name, Class<?> type, Method getterMethod) {
        this.name = name;
        this.type = type;
        this.getterMethod = getterMethod;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public Method getGetterMethod() {
        return getterMethod;
    }
}
