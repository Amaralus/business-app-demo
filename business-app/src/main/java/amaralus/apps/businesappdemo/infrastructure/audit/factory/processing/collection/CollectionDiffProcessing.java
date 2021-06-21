package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.collection;

import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.State;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;

import java.util.Collection;
import java.util.Iterator;

import static amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.collection.CollectionDiffProcessing.DiffPair;

public class CollectionDiffProcessing extends AbstractCollectionProcessing<DiffPair> {

    public CollectionDiffProcessing(FieldMetadata collectionMetadata, Collection<Object> oldCollection, Collection<Object> newCollection) {
        super(collectionMetadata, () -> diffIterator(oldCollection, newCollection));
    }

    // todo доделать
    private static Iterator<DiffPair> diffIterator(Collection<Object> oldCollection, Collection<Object> newCollection) {
        return null;
    }

    // todo доделать
    @Override
    protected State processAuditEntity(DiffPair diffPair) {
        return null;
    }

    // todo доделать
    @Override
    protected void processObject(DiffPair diffPair) {

    }

    protected static class DiffPair {
        final Object oldEntity;
        final Object newEntity;

        public DiffPair(Object oldEntity, Object newEntity) {
            this.oldEntity = oldEntity;
            this.newEntity = newEntity;
        }
    }
}
