package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.collection;

import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.IterableProcessing;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.State;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;

import java.util.Iterator;
import java.util.function.Supplier;

import static amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadataType.AUDIT_COLLECTION;

public abstract class AbstractCollectionProcessing<O> extends IterableProcessing<O> {

    protected final FieldMetadata collectionMetadata;
    private final boolean isAuditCollection;

    AbstractCollectionProcessing(FieldMetadata collectionMetadata, Supplier<Iterator<O>> iteratorSupplier) {
        super(iteratorSupplier);
        this.collectionMetadata = collectionMetadata;
        this.isAuditCollection = collectionMetadata.getType() == AUDIT_COLLECTION;
    }

    @Override
    protected void onIterationEnd() {
        if (currentIterationNumber == 0) {
            var prefix = paramNamePrefix;
            paramNamePrefix = null;
            addParam(prefix, "empty");
        }
        super.onIterationEnd();
    }

    @Override
    protected State getNextState(O entity) {
        if (isAuditCollection)
            return processAuditEntity(entity);
        else {
            processObject(entity);
            return null;
        }
    }

    protected abstract State processAuditEntity(O entity);

    protected abstract void processObject(O entity);
}
