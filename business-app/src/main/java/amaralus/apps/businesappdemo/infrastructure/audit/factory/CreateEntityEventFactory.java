package amaralus.apps.businesappdemo.infrastructure.audit.factory;

import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.AuditEntityProcessingStrategy;
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

        var entityStrategy = new AuditEntityProcessingStrategy(eventData.getEntityMetadata(), eventData.getNewAuditEntity());
        stateMachine.addState(entityStrategy);

        stateMachine.executeAll();

        entityStrategy.getParams().forEach(auditLibraryEventBuilder::param);
        return auditLibraryEventBuilder.build();
    }

    @Override
    public Type getFactoryType() {
        return Type.CREATE_ENTITY_FACTORY;
    }
}
