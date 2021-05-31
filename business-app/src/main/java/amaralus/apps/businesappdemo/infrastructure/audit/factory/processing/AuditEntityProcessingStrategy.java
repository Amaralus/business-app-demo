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
            var strategy = getProcessingStrategy(fields.next());
            if (useParentNamePrefix)
                strategy.setParamNamePrefix(paramNamePrefix);
            stateMachine.addState(strategy);
        }
        else
            returnParams();
    }

    private FieldProcessingStrategy getProcessingStrategy(FieldMetadata fieldMetadata) {
        switch (fieldMetadata.getType()) {
            case AUDIT_ENTITY:
                return newAuditProcessingStrategy(fieldMetadata);
            case COLLECTION:
            case MAP:
            default:
                return new ObjectProcessingStrategy(fieldMetadata, entity);
        }
    }

    private AuditEntityProcessingStrategy newAuditProcessingStrategy(FieldMetadata fieldMetadata) {
        var strategy = new AuditEntityProcessingStrategy(fieldMetadata.getEntityMetadataLink(), extractData(fieldMetadata, entity));
        strategy.setParamNamePrefix(fieldMetadata.getParamName());
        strategy.setUseParentNamePrefix(true);
        return strategy;
    }

    public void setUseParentNamePrefix(boolean useParentNamePrefix) {
        this.useParentNamePrefix = useParentNamePrefix;
    }
}
