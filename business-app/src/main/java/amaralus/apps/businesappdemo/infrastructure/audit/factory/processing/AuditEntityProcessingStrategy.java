package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing;

import amaralus.apps.businesappdemo.infrastructure.audit.metadata.EntityMetadata;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;

@Slf4j
public class AuditEntityProcessingStrategy extends FieldProcessingStrategy {

    private final Object entity;
    private final Iterator<FieldMetadata> fields;

    private boolean useParentNamePrefix;

    public AuditEntityProcessingStrategy(EntityMetadata entityMetadata, Object entity) {
        this.entity = entity;
        fields = entityMetadata.getFieldsMetadata().iterator();
    }

    @Override
    void update() {
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

    private ObjectProcessingStrategy objectStrategy(FieldMetadata fieldMetadata) {
        var strategy = new ObjectProcessingStrategy(fieldMetadata, entity);
        if (useParentNamePrefix)
            strategy.setParamNamePrefix(paramNamePrefix);
        return strategy;
    }

    private AuditEntityProcessingStrategy auditEntityStrategy(FieldMetadata fieldMetadata) {
        var strategy = new AuditEntityProcessingStrategy(fieldMetadata.getEntityMetadataLink(), extractData(fieldMetadata, entity));
        strategy.setUseParentNamePrefix(true);
        strategy.setParamNamePrefix(updateName(fieldMetadata.getParamName()));
        return strategy;
    }

    public void setUseParentNamePrefix(boolean useParentNamePrefix) {
        this.useParentNamePrefix = useParentNamePrefix;
    }
}
