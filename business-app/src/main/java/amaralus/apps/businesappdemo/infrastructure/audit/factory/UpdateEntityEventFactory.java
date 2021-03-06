package amaralus.apps.businesappdemo.infrastructure.audit.factory;

import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.StateMachine;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.auditentity.AuditEntityDiffProcessing;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent.AuditLibraryEventBuilder;

import static amaralus.apps.businesappdemo.infrastructure.audit.metadata.MetadataUtil.getEventCode;

public class UpdateEntityEventFactory implements EventFactory {

    @Override
    public AuditLibraryEvent produce(EventData eventData) {
        var auditLibraryEventBuilder = new AuditLibraryEventBuilder(
                eventData.getEntityMetadata().getGroupCode(),
                getEventCode("update", eventData.getEntityMetadata().getEntityClass()),
                eventData.isSuccess())
                .fillAuditContextUUID(eventData.getAuditContextUuid().toString());

        var stateMachine = new StateMachine();

        var entityStrategy = new AuditEntityDiffProcessing(
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
