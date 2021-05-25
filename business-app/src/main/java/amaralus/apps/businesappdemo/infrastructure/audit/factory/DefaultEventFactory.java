package amaralus.apps.businesappdemo.infrastructure.audit.factory;

import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent.AuditLibraryEventBuilder;
import org.springframework.stereotype.Component;

@Component
public class DefaultEventFactory implements EventFactory {

    @Override
    public AuditLibraryEvent produce(EventData eventData) {
        var auditLibraryEventBuilder = new AuditLibraryEventBuilder(eventData.getGroupCode(), eventData.getEventCode(), eventData.isSuccess());
        eventData.getParams().forEach(auditLibraryEventBuilder::param);
        return auditLibraryEventBuilder.build();
    }

    @Override
    public Type getFactoryType() {
        return Type.DEFAULT_ENTITY_FACTORY;
    }
}
