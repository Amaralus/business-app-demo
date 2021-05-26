package amaralus.apps.businesappdemo.infrastructure.audit.factory;

import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.ObjectProcessingStrategy;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent.AuditLibraryEventBuilder;

import java.util.Objects;

public class UpdateEntityEventFactory implements EventFactory {

    @Override
    public AuditLibraryEvent produce(EventData eventData) {
        var auditLibraryEventBuilder = new AuditLibraryEventBuilder(
                eventData.getEntityMetadata().getGroupCode(),
                "update" + eventData.getEntityMetadata().getEntityClass().getSimpleName(),
                eventData.isSuccess());

        var objectProcessingStrategy = new ObjectProcessingStrategy();
        for (var metadata : eventData.getEntityMetadata().getFieldsMetadata()) {
            // todo AuditEntity processing, collection processing, map processing
            // пока что все воспринимаем как объект
            var oldValue = objectProcessingStrategy.process(metadata, eventData.getOldAuditEntity());
            var newValue = objectProcessingStrategy.process(metadata, eventData.getNewAuditEntity());

            var diff = getDiff(oldValue, newValue);

            if (diff != null)
                auditLibraryEventBuilder.param(metadata.getParamName(), diff);
        }

        return auditLibraryEventBuilder.build();
    }

    private String getDiff(Object oldValue, Object newValue) {
        if (Objects.equals(oldValue, newValue))
            return null;
        else
            return oldValue + " -> " + newValue;
    }

    @Override
    public Type getFactoryType() {
        return Type.UPDATE_ENTITY_FACTORY;
    }
}
