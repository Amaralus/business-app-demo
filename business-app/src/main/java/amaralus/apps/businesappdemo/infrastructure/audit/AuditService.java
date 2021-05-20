package amaralus.apps.businesappdemo.infrastructure.audit;

import amaralus.apps.businesappdemo.infrastructure.audit.context.AuditContext;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.AbstractEventFactory;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.FactoryType;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.EntityMetadata;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryServiceStub;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static amaralus.apps.businesappdemo.infrastructure.audit.EventType.DELETE;
import static amaralus.apps.businesappdemo.infrastructure.audit.EventType.SAVE;
import static amaralus.apps.businesappdemo.infrastructure.audit.factory.FactoryType.*;

@Service
@Slf4j
public class AuditService {

    private final AuditContext auditContext;
    private final AuditLibraryServiceStub auditLibraryServiceStub;

    public AuditService(AuditContext auditContext, AuditLibraryServiceStub auditLibraryServiceStub) {
        this.auditContext = auditContext;
        this.auditLibraryServiceStub = auditLibraryServiceStub;
    }

    public EventSender successEvent(EventType eventType) {
        return new EventSender(true, eventType);
    }

    public EventSender failEvent(EventType eventType) {
        return new EventSender(false, eventType);
    }

    public class EventSender {

        private final boolean success;
        private final EventType eventType;

        private Object oldAuditEntity;
        private Object newAuditEntity;
        private EntityMetadata entityMetadata;

        private AuditLibraryEvent event;

        private EventSender(boolean success, EventType eventType) {
            this.success = success;
            this.eventType = eventType;
        }

        public EventSender entity(Object auditEntity) {
            return entity(auditEntity, null);
        }

        public EventSender entity(Object newAuditEntity, Object oldAuditEntity) {
            this.newAuditEntity = newAuditEntity;
            this.oldAuditEntity = oldAuditEntity;
            return this;
        }

        public void send() {
            try {
                prepareEvent();
                if (event != null)
                    auditLibraryServiceStub.send(event);
            } catch (Exception e) {
                log.error("Ошибка во время отправки собития аудита", e);
            }
        }

        private void prepareEvent() {
            if (eventType == SAVE || eventType == DELETE) {
                if (!auditContext.containsMetadata(newAuditEntity.getClass())) {
                    log.warn("Нет метамодели для сущности [{}]", newAuditEntity.getClass().getName());
                    return;
                }
                entityMetadata = auditContext.getMetadata(newAuditEntity.getClass());
            }

            event = AbstractEventFactory.createEventFactory(getFactoryType())
                    .success(success)
                    .oldAuditEntity(oldAuditEntity)
                    .newAuditEntity(newAuditEntity)
                    .entityMetadata(entityMetadata)
                    .produce();
        }

        private FactoryType getFactoryType() {
            switch (eventType) {
                case SAVE:
                    return oldAuditEntity == null ? CREATE_ENTITY_FACTORY : UPDATE_ENTITY_FACTORY;
                case DELETE:
                    return DELETE_ENTITY_FACTORY;
                default:
                    return DEFAULT_ENTITY_FACTORY;
            }
        }
    }
}
