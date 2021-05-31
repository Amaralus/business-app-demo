package amaralus.apps.businesappdemo.infrastructure.audit.factory;

import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.ObjectProcessingStrategy;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.StateMachine;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent;

public class CreateEntityEventFactory implements EventFactory {

    @Override
    public AuditLibraryEvent produce(EventData eventData) {
        var auditLibraryEventBuilder = new AuditLibraryEvent.AuditLibraryEventBuilder(
                eventData.getEntityMetadata().getGroupCode(),
                "create" + eventData.getEntityMetadata().getEntityClass().getSimpleName(),
                eventData.isSuccess());

        var stateMachine = new StateMachine();
        for (var metadata : eventData.getEntityMetadata().getFieldsMetadata()) {
            // todo AuditEntity processing, collection processing, map processing
            // пока что все воспринимаем как объект
            var objectProcessingStrategy = new ObjectProcessingStrategy(stateMachine, metadata, eventData.getNewAuditEntity());
            stateMachine.addState(objectProcessingStrategy);
            objectProcessingStrategy.update();

            auditLibraryEventBuilder.param(metadata.getParamName(), wrapNull(objectProcessingStrategy.getParams().get(metadata.getParamName())));
        }

        return auditLibraryEventBuilder.build();
    }

    private Object wrapNull(Object object) {
        return object == null ? "null": object;
    }

    @Override
    public Type getFactoryType() {
        return Type.CREATE_ENTITY_FACTORY;
    }
}
