package amaralus.apps.businesappdemo.infrastructure.audit.context;

import amaralus.apps.businesappdemo.infrastructure.audit.AuditEntity;
import amaralus.apps.businesappdemo.infrastructure.audit.AuditExclude;
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
        if (field.getAnnotation(AuditExclude.class) != null) {
            log.debug("Field [{}] was excluded", field.getName());
            return null;
        }

        var getter = BeanUtils.getPropertyDescriptor(entityClass, field.getName()).getReadMethod();

        if (getter == null) {
            log.debug("Field [{}] was skipped, because getter not found", field.getName());
            return null;
        }

        return new FieldMetadata(field.getName(), field.getType(), getter);
    }

    public Map<Class<?>, EntityMetadata> getEntitiesMetadata() {
        return entitiesMetadata;
    }
}
