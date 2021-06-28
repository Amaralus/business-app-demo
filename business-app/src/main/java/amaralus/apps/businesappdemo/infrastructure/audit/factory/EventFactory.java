package amaralus.apps.businesappdemo.infrastructure.audit.factory;

import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent;

public interface EventFactory {

    AuditLibraryEvent produce(EventData eventData);

    Type getFactoryType();

    enum Type {
        DEFAULT_ENTITY_FACTORY,
        CREATE_ENTITY_FACTORY,
        UPDATE_ENTITY_FACTORY,
        DELETE_ENTITY_FACTORY
    }
}
