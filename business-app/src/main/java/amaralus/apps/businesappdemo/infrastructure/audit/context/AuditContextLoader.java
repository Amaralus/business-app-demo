package amaralus.apps.businesappdemo.infrastructure.audit.context;

import amaralus.apps.businesappdemo.infrastructure.audit.AuditEntity;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.EntityMetadata;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

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
            var getter = BeanUtils.getPropertyDescriptor(entityClass, field.getName()).getReadMethod();

            if (getter == null) {
                log.debug("Field [{}] was skipped, because getter not found", field.getName());
                continue;
            }

            var metadata = new FieldMetadata(field.getName(), field.getType(), getter);
            fieldsMetadata.add(metadata);
        }
        return new EntityMetadata(entityClass, fieldsMetadata);
    }

    public Map<Class<?>, EntityMetadata> getEntitiesMetadata() {
        return entitiesMetadata;
    }
}
