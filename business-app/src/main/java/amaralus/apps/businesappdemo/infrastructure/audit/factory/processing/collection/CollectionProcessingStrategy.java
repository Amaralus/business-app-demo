package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.collection;

import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.ProcessingStrategy;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.State;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.object.ObjectProcessingStrategy;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;

import java.util.Collection;
import java.util.Iterator;

import static amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadataType.AUDIT_COLLECTION;

public class CollectionProcessingStrategy extends ProcessingStrategy {

    private final FieldMetadata collectionMetadata;
    private final Iterator<Object> entities;
    private final boolean isAuditCollection;

    private int currentEntityNumber;

    public CollectionProcessingStrategy(FieldMetadata collectionMetadata, Collection<Object> entities) {
        this.collectionMetadata = collectionMetadata;
        this.entities = entities.iterator();
        this.isAuditCollection = collectionMetadata.getType() == AUDIT_COLLECTION;
    }

    @Override
    public void execute() {
        if (entities.hasNext()) {
            var state = getState(entities.next());
            if (state != null)
                stateMachine.addState(state);
            ++currentEntityNumber;
        } else {
            if (currentEntityNumber == 0) {
                var prefix = paramNamePrefix;
                paramNamePrefix = null;
                addParam(prefix, "empty");
            }
            returnParams();
        }
    }

    private State getState(Object entity) {
        if (isAuditCollection) {
            var state = new ObjectProcessingStrategy(collectionMetadata.getEntityMetadataLink().getIdFieldMetadata(), entity);
            state.setParamNamePrefix( updateName(String.valueOf(currentEntityNumber)));
            return state;
        } else {
            addParam(String.valueOf(currentEntityNumber), wrapNull(entity));
            return null;
        }
    }
}
