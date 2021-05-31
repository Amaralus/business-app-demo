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

    public AuditEntityProcessingStrategy(EntityMetadata entityMetadata, Object entity) {
        this.entityMetadata = entityMetadata;
        this.entity = entity;
        fields = entityMetadata.getFieldsMetadata().iterator();
    }

    @Override
    void update() {
        if (fields.hasNext())
            stateMachine.addState(new ObjectProcessingStrategy(fields.next(), entity));
        else
            stateMachine.removeState();
    }
}
