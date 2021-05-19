package amaralus.apps.businesappdemo.infrastructure.audit.context;

import amaralus.apps.businesappdemo.infrastructure.audit.metadata.EntityMetadata;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuditContext {

    private final Map<Class<?>, EntityMetadata> entitiesMetadata = new ConcurrentHashMap<>();

    public AuditContext(Map<Class<?>, EntityMetadata> entitiesMetadata) {
        this.entitiesMetadata.putAll(entitiesMetadata);
    }
}
