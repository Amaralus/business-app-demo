package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.auditentity;

import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.State;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.object.ObjectDiffProcessingStrategy;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.EntityMetadata;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;

public class AuditEntityDiffProcessingStrategy extends  AuditEntityProcessingStrategy {

    private final Object oldEntity;
    private final FieldMetadata idField;

    private boolean deepDiff;
    private boolean idExtracted;

    public AuditEntityDiffProcessingStrategy(EntityMetadata entityMetadata, Object oldEntity, Object entity) {
        super(entityMetadata, entity);
        this.idField = entityMetadata.getIdFieldMetadata();
        this.oldEntity = oldEntity;
    }

    public AuditEntityDiffProcessingStrategy(EntityMetadata entityMetadata, int walkDepth, Object oldEntity, Object entity) {
        super(entityMetadata, walkDepth, entity);
        this.idField = entityMetadata.getIdFieldMetadata();
        this.oldEntity = oldEntity;
    }

    @Override
    public void execute() {
        if (oldEntity == null && entity == null) {
            stateMachine.removeState();
            return;
        } else if (oldEntity != null && entity != null)
            deepDiff = true;

        if (deepDiff)
            super.execute();
        else {
            if (idExtracted)
                returnParams();
            else
                extractId();
        }
    }

    private void extractId() {
        State state;
        if (oldEntity == null)
            state = diffObjectStrategy(idField, null, entity);
        else
            state = diffObjectStrategy(idField, oldEntity, null);
        stateMachine.addState(state);
        idExtracted = true;
    }

    @Override
    protected State objectStrategy(FieldMetadata fieldMetadata) {
        return diffObjectStrategy(fieldMetadata, oldEntity, entity);
    }

    private ObjectDiffProcessingStrategy diffObjectStrategy(FieldMetadata fieldMetadata, Object oldEntity, Object entity) {
        var strategy = new ObjectDiffProcessingStrategy(fieldMetadata, oldEntity, entity);
        if (useParentNamePrefix)
            strategy.setParamNamePrefix(paramNamePrefix);
        return strategy;
    }

    @Override
    protected State auditEntityStrategy(FieldMetadata fieldMetadata) {
        var strategy = new AuditEntityDiffProcessingStrategy(
                fieldMetadata.getEntityMetadataLink(),
                walkDepth - 1,
                extractData(fieldMetadata, oldEntity),
                extractData(fieldMetadata, entity));

        strategy.setUseParentNamePrefix(true);
        strategy.setParamNamePrefix(updateName(fieldMetadata.getParamName()));
        return strategy;
    }
}
