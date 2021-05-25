package amaralus.apps.businesappdemo.infrastructure.audit;

import amaralus.apps.businesappdemo.infrastructure.audit.context.AuditContext;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.EventData;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.EventFactory;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryServiceStub;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static amaralus.apps.businesappdemo.infrastructure.audit.EventType.DELETE;
import static amaralus.apps.businesappdemo.infrastructure.audit.EventType.SAVE;
import static amaralus.apps.businesappdemo.infrastructure.audit.factory.EventFactory.Type.*;

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

        private final EventData eventData;
        private final EventType eventType;

        private AuditLibraryEvent event;

        private EventSender(boolean success, EventType eventType) {
            this.eventData = new EventData();
            eventData.setSuccess(success);
            this.eventType = eventType;
        }

        public EventSender groupCode(String groupCode) {
            eventData.setGroupCode(groupCode);
            return this;
        }

        public EventSender eventCode(String eventCode) {
            eventData.setEventCode(eventCode);
            return this;
        }

        public EventSender param(String name, Object value) {
            eventData.addParam(name, value.toString());
            return this;
        }

        public EventSender entity(Object auditEntity) {
            return entity(auditEntity, null);
        }

        public EventSender entity(Object newAuditEntity, Object oldAuditEntity) {
            eventData.setNewAuditEntity(newAuditEntity);
            eventData.setOldAuditEntity(oldAuditEntity);
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
                if (!auditContext.containsMetadata(eventData.getNewAuditEntity().getClass())) {
                    log.warn("Нет метамодели для сущности [{}]", eventData.getNewAuditEntity().getClass().getName());
                    return;
                }
                var entityMetadata = auditContext.getMetadata(eventData.getNewAuditEntity().getClass());
                eventData.setEntityMetadata(entityMetadata);
            }

            event = auditContext.getEventFactory(getFactoryType()).produce(eventData);
        }

        private EventFactory.Type getFactoryType() {
            switch (eventType) {
                case SAVE:
                    return eventData.getOldAuditEntity() == null ? CREATE_ENTITY_FACTORY : UPDATE_ENTITY_FACTORY;
                case DELETE:
                    return DELETE_ENTITY_FACTORY;
                default:
                    return DEFAULT_ENTITY_FACTORY;
            }
        }
    }
}
