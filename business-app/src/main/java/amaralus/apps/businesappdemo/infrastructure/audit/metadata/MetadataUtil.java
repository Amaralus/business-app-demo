package amaralus.apps.businesappdemo.infrastructure.audit.metadata;

import amaralus.apps.businesappdemo.infrastructure.audit.AuditEntity;
import amaralus.apps.businesappdemo.infrastructure.audit.AuditId;
import amaralus.apps.businesappdemo.infrastructure.audit.AuditParam;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

public final class MetadataUtil {

    private MetadataUtil() {}

    public static boolean isAuditEntity(Class<?> clazz) {
        return clazz.getAnnotation(AuditEntity.class) != null;
    }

    public static boolean isAuditParam(Field field) {
        return field.getAnnotation(AuditParam.class) != null;
    }

    public static boolean isAuditId(Field field) {
        return field.getAnnotation(AuditId.class) != null;
    }

    public static Method getGetter(Field field) {
        return BeanUtils.getPropertyDescriptor(field.getDeclaringClass(), field.getName()).getReadMethod();
    }

    public static Class<?> getFieldGenericType(Field field, int genericNumber) {
        var type = (ParameterizedType) field.getGenericType();
        return (Class<?>) type.getActualTypeArguments()[genericNumber];
    }
}
