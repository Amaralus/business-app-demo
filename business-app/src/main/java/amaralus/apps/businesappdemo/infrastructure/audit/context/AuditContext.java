package amaralus.apps.businesappdemo.infrastructure.audit.context;

import amaralus.apps.businesappdemo.infrastructure.audit.factory.EventFactory;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.EntityMetadata;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuditContext {

    private final Map<Class<?>, EntityMetadata> entitiesMetadata = new ConcurrentHashMap<>();
    private final Map<EventFactory.Type, EventFactory> factories = new ConcurrentHashMap<>();

    public AuditContext(Map<Class<?>, EntityMetadata> entitiesMetadata, List<EventFactory> eventFactoryList) {
        this.entitiesMetadata.putAll(entitiesMetadata);
        eventFactoryList.forEach(factory -> factories.put(factory.getFactoryType(), factory));
    }

    public EntityMetadata getMetadata(Class<?> entityClass) {
        return entitiesMetadata.get(entityClass);
    }

    public boolean containsMetadata(Class<?> entityClass) {
        return entitiesMetadata.containsKey(entityClass);
    }

    public EventFactory getEventFactory(EventFactory.Type type) {
        return factories.get(type);
    }
}
