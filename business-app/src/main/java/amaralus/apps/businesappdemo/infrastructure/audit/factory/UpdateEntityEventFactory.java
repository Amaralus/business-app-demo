package amaralus.apps.businesappdemo.infrastructure.audit.factory;

import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.StateMachine;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.auditentity.AuditEntityDiffProcessingStrategy;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent.AuditLibraryEventBuilder;

public class UpdateEntityEventFactory implements EventFactory {

    @Override
    public AuditLibraryEvent produce(EventData eventData) {
        var auditLibraryEventBuilder = new AuditLibraryEventBuilder(
                eventData.getEntityMetadata().getGroupCode(),
                "update" + eventData.getEntityMetadata().getEntityClass().getSimpleName(),
                eventData.isSuccess());

        var stateMachine = new StateMachine();

        var entityStrategy = new AuditEntityDiffProcessingStrategy(
                eventData.getEntityMetadata(),
                eventData.getOldAuditEntity(),
                eventData.getNewAuditEntity());

        stateMachine.addState(entityStrategy);
        stateMachine.executeAll();

        entityStrategy.getParams().forEach(auditLibraryEventBuilder::param);
        return auditLibraryEventBuilder.build();
    }

    @Override
    public Type getFactoryType() {
        return Type.UPDATE_ENTITY_FACTORY;
    }
}
