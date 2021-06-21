package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.auditentity;

import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.ProcessingStrategy;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.State;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.collection.CollectionProcessing;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.object.ObjectProcessing;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.EntityMetadata;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Iterator;

@Slf4j
public class AuditEntityProcessing extends ProcessingStrategy {

    protected final Object entity;
    protected final int walkDepth;
    protected final Iterator<FieldMetadata> fields;

    protected boolean useParentNamePrefix;

    public AuditEntityProcessing(EntityMetadata entityMetadata, Object entity) {
        this(entityMetadata, entityMetadata.getWalkDepth(), entity);
    }

    public AuditEntityProcessing(EntityMetadata entityMetadata, int walkDepth, Object entity) {
        this.entity = entity;
        this.walkDepth = walkDepth;
        fields = entityMetadata.getFieldsMetadata().iterator();
    }

    @Override
    public void execute() {
        if (fields.hasNext()) {
            var fieldMetadata = fields.next();
            State state;
            switch (fieldMetadata.getType()) {
                case AUDIT_ENTITY:
                    if (walkDepth == 0)
                        return;
                    state = auditEntityStrategy(fieldMetadata);
                    break;
                case COLLECTION:
                case AUDIT_COLLECTION:
                    state = collectionProcessingStrategy(fieldMetadata);
                    break;
                default:
                    state = objectStrategy(fieldMetadata);
                    break;
            }
            stateMachine.addState(state);
        } else
            returnParams();
    }

    protected State objectStrategy(FieldMetadata fieldMetadata) {
        var strategy = new ObjectProcessing(fieldMetadata, entity);
        if (useParentNamePrefix)
            strategy.setParamNamePrefix(paramNamePrefix);
        return strategy;
    }

    protected State auditEntityStrategy(FieldMetadata fieldMetadata) {
        var fieldValue = extractData(fieldMetadata, entity);
        if (fieldValue == null)
            return objectStrategy(fieldMetadata);

        var strategy = new AuditEntityProcessing(fieldMetadata.getEntityMetadataLink(), walkDepth - 1, fieldValue);
        strategy.setUseParentNamePrefix(true);
        strategy.setParamNamePrefix(updateName(fieldMetadata.getParamName()));
        return strategy;
    }

    // todo diff collections
    protected State collectionProcessingStrategy(FieldMetadata fieldMetadata) {
        var strategy = new CollectionProcessing(fieldMetadata, (Collection<Object>) extractData(fieldMetadata, entity));
        strategy.setParamNamePrefix(updateName(fieldMetadata.getParamName()));
        return strategy;
    }

    public void setUseParentNamePrefix(boolean useParentNamePrefix) {
        this.useParentNamePrefix = useParentNamePrefix;
    }
}
