package amaralus.apps.businesappdemo.infrastructure.audit.context;

import amaralus.apps.businesappdemo.infrastructure.audit.factory.EventFactory;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.EntityMetadata;

public interface AuditContext {

    boolean containsMetadata(Class<?> entityClass);

    EntityMetadata getMetadata(Class<?> entityClass);

    EventFactory getEventFactory(EventFactory.Type type);
}
