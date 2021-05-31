package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing;

import amaralus.apps.businesappdemo.infrastructure.audit.metadata.EntityMetadata;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;

@Slf4j
public class AuditEntityProcessingStrategy extends FieldProcessingStrategy {

    private final EntityMetadata entityMetadata;
    private final Object entity;
    private final Iterator<FieldMetadata> fields;

    private boolean useParentNamePrefix;

    public AuditEntityProcessingStrategy(EntityMetadata entityMetadata, Object entity) {
        this.entityMetadata = entityMetadata;
        this.entity = entity;
        fields = entityMetadata.getFieldsMetadata().iterator();
    }

    @Override
    void update() {
        if (fields.hasNext()) {
            var state = new ObjectProcessingStrategy(fields.next(), entity);
            if (useParentNamePrefix)
                state.setParamNamePrefix(paramNamePrefix);
            stateMachine.addState(state);
        }
        else
            returnParams();
    }

    public void setUseParentNamePrefix(boolean useParentNamePrefix) {
        this.useParentNamePrefix = useParentNamePrefix;
    }
}
