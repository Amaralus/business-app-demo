package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.auditentity;

import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.State;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.collection.AbstractCollectionProcessing;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.collection.CollectionProcessing;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.object.ObjectProcessing;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.EntityMetadata;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Slf4j
public class AuditEntityProcessing extends AbstractEntityProcessing {

    protected final Object entity;

    public AuditEntityProcessing(EntityMetadata entityMetadata, Object entity) {
        this(entityMetadata, entity, entityMetadata.getWalkDepth());
    }

    public AuditEntityProcessing(EntityMetadata entityMetadata, Object entity, int walkDepth) {
        super(entityMetadata.getFieldsMetadata()::iterator, walkDepth);
        this.entity = entity;
    }

    @Override
    protected ObjectProcessing newObjectProcessing(FieldMetadata fieldMetadata) {
        return new ObjectProcessing(fieldMetadata, entity);
    }

    @Override
    protected State auditEntityStrategy(FieldMetadata fieldMetadata) {
        var fieldValue = extractData(fieldMetadata, entity);
        if (fieldValue == null)
            return objectStrategy(fieldMetadata);

        return super.auditEntityStrategy(fieldMetadata);
    }

    @Override
    protected AbstractEntityProcessing newAuditEntityProcessing(FieldMetadata fieldMetadata) {
        return new AuditEntityProcessing(fieldMetadata.getEntityMetadataLink(), extractData(fieldMetadata, entity), walkDepth - 1);
    }

    @Override
    protected AbstractCollectionProcessing<Object> newCollectionProcessing(FieldMetadata fieldMetadata) {
        return new CollectionProcessing(fieldMetadata, (Collection<Object>) extractData(fieldMetadata, entity));
    }
}
