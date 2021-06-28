package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.auditentity;

import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.State;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.collection.AbstractCollectionProcessing;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.collection.CollectionDiffProcessing;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.object.ObjectDiffProcessing;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.object.ObjectProcessing;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.EntityMetadata;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;

import java.util.Collection;

public class AuditEntityDiffProcessing extends AbstractEntityProcessing {

    private final Object oldEntity;
    private final Object newEntity;
    private final FieldMetadata idField;

    private boolean deepDiff;
    private boolean idExtracted;

    public AuditEntityDiffProcessing(EntityMetadata entityMetadata, Object oldEntity, Object entity) {
        this(entityMetadata, oldEntity, entity, entityMetadata.getWalkDepth());
    }

    public AuditEntityDiffProcessing(EntityMetadata entityMetadata, Object oldEntity, Object newEntity, int walkDepth) {
        super(entityMetadata.getFieldsMetadata()::iterator, walkDepth);
        this.oldEntity = oldEntity;
        this.newEntity = newEntity;
        this.idField = entityMetadata.getIdFieldMetadata();
    }

    @Override
    public void execute() {
        if (oldEntity == null && newEntity == null) {
            stateMachine.removeState();
            return;
        } else if (oldEntity != null && newEntity != null)
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
            state = diffObjectStrategy(idField, null, newEntity);
        else
            state = diffObjectStrategy(idField, oldEntity, null);
        stateMachine.addState(state);
        idExtracted = true;
    }

    @Override
    protected ObjectProcessing newObjectProcessing(FieldMetadata fieldMetadata) {
        return diffObjectStrategy(fieldMetadata, oldEntity, newEntity);
    }

    private ObjectDiffProcessing diffObjectStrategy(FieldMetadata fieldMetadata, Object oldEntity, Object newEntity) {
        return new ObjectDiffProcessing(fieldMetadata, oldEntity, newEntity);
    }

    @Override
    protected AbstractEntityProcessing newAuditEntityProcessing(FieldMetadata fieldMetadata) {
        return new AuditEntityDiffProcessing(
                fieldMetadata.getEntityMetadataLink(),
                extractData(fieldMetadata, oldEntity),
                extractData(fieldMetadata, newEntity),
                walkDepth - 1);
    }

    // todo добавить потом дифф коллекций
    @Override
    protected AbstractCollectionProcessing<?> newCollectionProcessing(FieldMetadata fieldMetadata) {
        return new CollectionDiffProcessing(
                fieldMetadata,
                (Collection<Object>) extractData(fieldMetadata, oldEntity),
                (Collection<Object>)extractData(fieldMetadata, newEntity));
    }
}
