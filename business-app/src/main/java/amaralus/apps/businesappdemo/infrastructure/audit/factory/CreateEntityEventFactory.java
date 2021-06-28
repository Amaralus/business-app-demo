package amaralus.apps.businesappdemo.infrastructure.audit.factory;

import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.StateMachine;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.auditentity.AuditEntityProcessing;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent.AuditLibraryEventBuilder;

import static amaralus.apps.businesappdemo.infrastructure.audit.metadata.MetadataUtil.getEventCode;

public class CreateEntityEventFactory implements EventFactory {

    @Override
    public AuditLibraryEvent produce(EventData eventData) {
        var auditLibraryEventBuilder = new AuditLibraryEventBuilder(
                eventData.getEntityMetadata().getGroupCode(),
                getEventCode("create", eventData.getEntityMetadata().getEntityClass()),
                eventData.isSuccess())
                .fillAuditContextUUID(eventData.getAuditContextUuid().toString());

        var stateMachine = new StateMachine();

        var entityStrategy = new AuditEntityProcessing(eventData.getEntityMetadata(), eventData.getNewAuditEntity());
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
