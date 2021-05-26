package amaralus.apps.businesappdemo.infrastructure.audit.context.init;

import amaralus.apps.businesappdemo.infrastructure.audit.AuditEntity;
import amaralus.apps.businesappdemo.infrastructure.audit.AuditId;
import amaralus.apps.businesappdemo.infrastructure.audit.AuditParam;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.EntityMetadata;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadataType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static amaralus.apps.businesappdemo.infrastructure.audit.metadata.AuditEntityValidator.isValidEntity;
import static amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadataType.*;

@Slf4j
public class AuditContextLoader {

    private final Map<Class<?>, EntityMetadata> entitiesMetadata = new HashMap<>();
    private final String packageToScan;

    public AuditContextLoader(String packageToScan) {
        this.packageToScan = packageToScan;
    }

    public void loadAuditContextMetadata() {
        log.debug("Scanning audit entities in package [{}]", packageToScan);
        var scanner = new ClassPathScanningCandidateComponentProvider(false);

        scanner.addIncludeFilter(new AnnotationTypeFilter(AuditEntity.class));
        for (var definition : scanner.findCandidateComponents(packageToScan)) {
            log.debug("Found audit entity [{}]", definition.getBeanClassName());

            var entityClass = getEntityClass(definition);
            if (isValidEntity(entityClass)) {
                var entityMetadata = loadMetadata(entityClass);
                entitiesMetadata.put(entityClass, entityMetadata);
                log.debug("{}", entityMetadata);
            } else {
                log.warn("Invalid audit entity [{}] was skipped. Debug for more details", entityClass.getName());
            }
        }
    }

    private EntityMetadata loadMetadata(Class<?> entityClass) {
        var fieldsMetadata = new ArrayList<FieldMetadata>();

        for (var field : entityClass.getDeclaredFields()) {
            var fieldMetadata = loadFieldMetadata(entityClass, field);
            if (fieldMetadata != null)
                fieldsMetadata.add(fieldMetadata);
        }

        return new EntityMetadata(entityClass, fieldsMetadata);
    }

    private FieldMetadata loadFieldMetadata(Class<?> entityClass, Field field) {
        var fieldName = field.getName();
        var auditParam = field.getAnnotation(AuditParam.class);

        if (auditParam == null) {
            log.debug("Field [{}] was excluded", fieldName);
            return null;
        }

        var paramName = auditParam.name().isEmpty() ? fieldName : auditParam.name();
        var getter = BeanUtils.getPropertyDescriptor(entityClass, fieldName).getReadMethod();
        var idField = field.getAnnotation(AuditId.class) != null;
        var fieldType = defineFieldType(field);

        return new FieldMetadata(entityClass, fieldName, paramName, field.getType(), fieldType, getter, idField);
    }

    private FieldMetadataType defineFieldType(Field field) {
        var type = field.getType();

        if (Collection.class.isAssignableFrom(type)) return COLLECTION;
        if (Map.class.isAssignableFrom(type)) return MAP;

        if (type.getAnnotation(AuditEntity.class) != null)
            return AUDIT_ENTITY;
        else
            return OBJECT;
    }

    private Class<?> getEntityClass(BeanDefinition definition) {
        try {
            return Class.forName(definition.getBeanClassName());
        } catch (ClassNotFoundException e) {
            throw new AuditContextLoadingException(e);
        }
    }

    public Map<Class<?>, EntityMetadata> getEntitiesMetadata() {
        return entitiesMetadata;
    }
}
