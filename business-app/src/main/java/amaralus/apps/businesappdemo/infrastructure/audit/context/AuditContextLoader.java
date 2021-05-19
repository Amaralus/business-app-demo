package amaralus.apps.businesappdemo.infrastructure.audit.context;

import amaralus.apps.businesappdemo.infrastructure.audit.AuditEntity;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.EntityMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

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

            var entityClass = definition.getResolvableType().toClass();
            entitiesMetadata.put(entityClass, new EntityMetadata(entityClass));
        }
    }

    public Map<Class<?>, EntityMetadata> getEntitiesMetadata() {
        return entitiesMetadata;
    }
}
