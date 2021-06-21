package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing;

import amaralus.apps.businesappdemo.infrastructure.audit.metadata.EntityMetadata;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Iterator;

@Slf4j
public class AuditEntityProcessingStrategy extends FieldProcessingStrategy {

    protected final Object entity;
    protected final int walkDepth;
    protected final Iterator<FieldMetadata> fields;

    protected boolean useParentNamePrefix;

    public AuditEntityProcessingStrategy(EntityMetadata entityMetadata, Object entity) {
        this(entityMetadata, entityMetadata.getWalkDepth(), entity);
    }

    public AuditEntityProcessingStrategy(EntityMetadata entityMetadata, int walkDepth, Object entity) {
        this.entity = entity;
        this.walkDepth = walkDepth;
        fields = entityMetadata.getFieldsMetadata().iterator();
    }

    @Override
    void execute() {
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
                case MAP:
                default:
                    state = objectStrategy(fieldMetadata);
                    break;
            }
            stateMachine.addState(state);
        } else
            returnParams();
    }

    protected State objectStrategy(FieldMetadata fieldMetadata) {
        var strategy = new ObjectProcessingStrategy(fieldMetadata, entity);
        if (useParentNamePrefix)
            strategy.setParamNamePrefix(paramNamePrefix);
        return strategy;
    }

    protected State auditEntityStrategy(FieldMetadata fieldMetadata) {
        var fieldValue = extractData(fieldMetadata, entity);
        if (fieldValue == null)
            return objectStrategy(fieldMetadata);

        var strategy = new AuditEntityProcessingStrategy(fieldMetadata.getEntityMetadataLink(), walkDepth - 1, fieldValue);
        strategy.setUseParentNamePrefix(true);
        strategy.setParamNamePrefix(updateName(fieldMetadata.getParamName()));
        return strategy;
    }

    // todo diff collections
    protected State collectionProcessingStrategy(FieldMetadata fieldMetadata) {
        var strategy = new CollectionProcessingStrategy(fieldMetadata, (Collection<Object>) extractData(fieldMetadata, entity));
        strategy.setParamNamePrefix(updateName(fieldMetadata.getParamName()));
        return strategy;
    }

    public void setUseParentNamePrefix(boolean useParentNamePrefix) {
        this.useParentNamePrefix = useParentNamePrefix;
    }
}
