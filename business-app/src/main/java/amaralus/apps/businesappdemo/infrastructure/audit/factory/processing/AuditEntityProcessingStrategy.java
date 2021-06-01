package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing;

import amaralus.apps.businesappdemo.infrastructure.audit.metadata.EntityMetadata;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;

@Slf4j
public class AuditEntityProcessingStrategy extends FieldProcessingStrategy {

    protected final Object entity;
    protected final Iterator<FieldMetadata> fields;

    protected boolean useParentNamePrefix;

    public AuditEntityProcessingStrategy(EntityMetadata entityMetadata, Object entity) {
        this.entity = entity;
        fields = entityMetadata.getFieldsMetadata().iterator();
    }

    @Override
    void execute() {
        if (fields.hasNext()) {
            var fieldMetadata = fields.next();
            State state;
            switch (fieldMetadata.getType()) {
                case AUDIT_ENTITY:
                    state = auditEntityStrategy(fieldMetadata);
                    break;
                case COLLECTION:
                case MAP:
                default:
                    state = objectStrategy(fieldMetadata);
                    break;
            }
            stateMachine.addState(state);
        } else
            returnParams();
    }

    protected FieldProcessingStrategy objectStrategy(FieldMetadata fieldMetadata) {
        var strategy = new ObjectProcessingStrategy(fieldMetadata, entity);
        if (useParentNamePrefix)
            strategy.setParamNamePrefix(paramNamePrefix);
        return strategy;
    }

    protected FieldProcessingStrategy auditEntityStrategy(FieldMetadata fieldMetadata) {
        var fieldValue = extractData(fieldMetadata, entity);
        if (fieldValue == null)
            return objectStrategy(fieldMetadata);

        var strategy = new AuditEntityProcessingStrategy(fieldMetadata.getEntityMetadataLink(), fieldValue);
        strategy.setUseParentNamePrefix(true);
        strategy.setParamNamePrefix(updateName(fieldMetadata.getParamName()));
        return strategy;
    }

    public void setUseParentNamePrefix(boolean useParentNamePrefix) {
        this.useParentNamePrefix = useParentNamePrefix;
    }
}
