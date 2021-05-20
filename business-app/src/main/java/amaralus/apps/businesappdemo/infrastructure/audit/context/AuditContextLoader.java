package amaralus.apps.businesappdemo.infrastructure.audit.context;

import amaralus.apps.businesappdemo.infrastructure.audit.AuditEntity;
import amaralus.apps.businesappdemo.infrastructure.audit.AuditExclude;
import amaralus.apps.businesappdemo.infrastructure.audit.AuditId;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.EntityMetadata;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

            Class<?> entityClass;
            try {
                entityClass = Class.forName(definition.getBeanClassName());
            } catch (ClassNotFoundException e) {
                throw new AuditContextLoadingException(e);
            }
            var entityMetadata = loadMetadata(entityClass);
            entitiesMetadata.put(entityClass, entityMetadata);
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
        var name = field.getName();
        var exclude = field.getAnnotation(AuditExclude.class) != null;
        var idField = field.getAnnotation(AuditId.class) != null;

        if (exclude) {
            if (!idField) {
                log.debug("Field [{}] was excluded", name);
                return null;
            } else
                log.warn("Id-field [{}] can't be excluded", name);
        }

        var getter = BeanUtils.getPropertyDescriptor(entityClass, name).getReadMethod();

        if (getter == null) {
            if (!idField){
                log.debug("Field [{}] was skipped, because getter not found", name);
                return null;
            } else
                log.warn("Getter must exist for id-field [{}] to improve performance", name);
        }

        return new FieldMetadata(entityClass, name, field.getType(), getter, idField);
    }

    public Map<Class<?>, EntityMetadata> getEntitiesMetadata() {
        return entitiesMetadata;
    }
}
