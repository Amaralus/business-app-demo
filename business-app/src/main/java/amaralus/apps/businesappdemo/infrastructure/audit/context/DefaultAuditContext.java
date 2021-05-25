package amaralus.apps.businesappdemo.infrastructure.audit.context;

import amaralus.apps.businesappdemo.infrastructure.audit.factory.DefaultEventFactory;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.EventFactory;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.EntityMetadata;
import org.springframework.stereotype.Component;

@Component
public class DefaultAuditContext implements AuditContext {

    private final DefaultEventFactory defaultEventFactory;

    public DefaultAuditContext(DefaultEventFactory defaultEventFactory) {
        this.defaultEventFactory = defaultEventFactory;
    }

    @Override
    public boolean containsMetadata(Class<?> entityClass) {
        return false;
    }

    @Override
    public EntityMetadata getMetadata(Class<?> entityClass) {
        throw new UnsupportedOperationException("Default audit context doesn't support metadata processing");
    }

    @Override
    public EventFactory getEventFactory(EventFactory.Type type) {
        if (type == EventFactory.Type.DEFAULT_ENTITY_FACTORY)
            return defaultEventFactory;
        else
            throw new UnsupportedOperationException("Factory type [" + type + "] is unsupported");
    }
}
