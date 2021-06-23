package amaralus.apps.businesappdemo.infrastructure.audit;

import amaralus.apps.businesappdemo.infrastructure.audit.context.LocalAuditContext;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.EventData;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.EventFactory;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryServiceStub;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditSendException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static amaralus.apps.businesappdemo.infrastructure.audit.EventType.*;
import static amaralus.apps.businesappdemo.infrastructure.audit.factory.EventFactory.Type.*;

@Service
@Slf4j
public class AuditService {

    private final LocalAuditContext localAuditContext;
    private final AuditLibraryServiceStub auditLibraryServiceStub;
    private final AtomicBoolean catchExceptionsEnabled = new AtomicBoolean(true);

    public AuditService(LocalAuditContext localAuditContext, AuditLibraryServiceStub auditLibraryServiceStub) {
        this.localAuditContext = localAuditContext;
        this.auditLibraryServiceStub = auditLibraryServiceStub;
    }

    public EventSender successEvent() {
        return successEvent(SIMPLE);
    }

    public EventSender successEvent(EventType eventType) {
        return new EventSender(true, eventType);
    }

    public EventSender successEvent(SimpleAuditEvent simpleAuditEvent) {
        return new EventSender(true, simpleAuditEvent);
    }

    public EventSender failEvent() {
        return failEvent(SIMPLE);
    }

    public EventSender failEvent(EventType eventType) {
        return new EventSender(false, eventType);
    }

    public EventSender failEvent(SimpleAuditEvent simpleAuditEvent) {
        return new EventSender(false, simpleAuditEvent);
    }

    public void enableCatchExceptions(boolean enable) {
        catchExceptionsEnabled.set(enable);
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

        private EventSender(boolean success, SimpleAuditEvent simpleAuditEvent) {
            this(success, SIMPLE);
            groupCode(simpleAuditEvent.getGroupCode());
            eventCode(simpleAuditEvent.getEventCode());
        }

        public EventSender groupCode(String groupCode) {
            eventData.setGroupCode(Objects.requireNonNull(groupCode));
            return this;
        }

        public EventSender eventCode(String eventCode) {
            eventData.setEventCode(Objects.requireNonNull(eventCode));
            return this;
        }

        public EventSender contextUuid(UUID auditContextUUID) {
            if (auditContextUUID != null)
                eventData.setAuditContextUuid(auditContextUUID);
            return this;
        }

        public EventSender param(String name, Object value) {
            eventData.addParam(Objects.requireNonNull(name), value == null ? null : value.toString());
            return this;
        }

        public EventSender entity(Object auditEntity) {
            return entity(null, auditEntity);
        }

        public EventSender entity(Object oldAuditEntity, Object newAuditEntity) {
            eventData.setNewAuditEntity(newAuditEntity);
            eventData.setOldAuditEntity(oldAuditEntity);
            return this;
        }

        public void send() {
            try {
                prepareEvent();
                if (event != null)
                    auditLibraryServiceStub.send(event);
            } catch (AuditSendException e) {
                if (catchExceptionsEnabled.get())
                    log.error("Ошибка отправки собития аудита", e);
                else
                    // для прототипа сойдет, в конечной реализации кинем бизнес эксепшн
                    throw new RuntimeException(e);
            } catch (Exception e) {
                log.error("Непредвиденная ошибка во время отправки события аудита", e);
            }
        }

        private void prepareEvent() {
            if (eventType == SAVE_ENTITY || eventType == DELETE_ENTITY) {
                if (eventData.getNewAuditEntity() == null) {
                    log.warn("Отсутствует аудируемая сущность");
                    return;
                }

                if (!localAuditContext.containsMetadata(eventData.getNewAuditEntity().getClass())) {
                    log.warn("Нет метаданных для сущности [{}]", eventData.getNewAuditEntity().getClass().getName());
                    return;
                }
                var entityMetadata = localAuditContext.getMetadata(eventData.getNewAuditEntity().getClass());
                eventData.setEntityMetadata(entityMetadata);
            } else if (eventData.getGroupCode() == null || eventData.getEventCode() == null) {
                log.warn("Не заполнено обязательное поле события: [{}]", eventData.getGroupCode() == null ? "groupCode" : "eventCode");
                return;
            }

            event = localAuditContext.getEventFactory(getFactoryType()).produce(eventData);
        }

        private EventFactory.Type getFactoryType() {
            switch (eventType) {
                case SAVE_ENTITY:
                    return eventData.getOldAuditEntity() == null ? CREATE_ENTITY_FACTORY : UPDATE_ENTITY_FACTORY;
                case DELETE_ENTITY:
                    return DELETE_ENTITY_FACTORY;
                default:
                    return DEFAULT_ENTITY_FACTORY;
            }
        }
    }
}
