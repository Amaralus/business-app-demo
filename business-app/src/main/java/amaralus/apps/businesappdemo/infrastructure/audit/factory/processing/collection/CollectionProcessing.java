package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.collection;

import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.State;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.object.ObjectProcessing;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;

import java.util.Collection;

public class CollectionProcessing extends AbstractCollectionProcessing<Object> {

    public CollectionProcessing(FieldMetadata collectionMetadata, Collection<Object> entities) {
        super(collectionMetadata, entities::iterator);
    }


    @Override
    protected State processAuditEntity(Object entity) {
        var state = new ObjectProcessing(collectionMetadata.getEntityMetadataLink().getIdFieldMetadata(), entity);
        state.setParamNamePrefix(updateName(String.valueOf(currentIterationNumber)));
        return state;
    }

    @Override
    protected void processObject(Object entity) {
        addParam(String.valueOf(currentIterationNumber), wrapNull(entity));
    }
}
