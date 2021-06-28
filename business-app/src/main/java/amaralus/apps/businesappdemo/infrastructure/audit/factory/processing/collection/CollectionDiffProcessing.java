package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.collection;

import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.DiffProcessing;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.State;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.object.ObjectDiffProcessing;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;

import java.util.*;
import java.util.stream.Collectors;

import static amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.collection.CollectionDiffProcessing.DiffPair;
import static amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadataType.AUDIT_COLLECTION;

public class CollectionDiffProcessing extends AbstractCollectionProcessing<DiffPair> implements DiffProcessing {

    public CollectionDiffProcessing(FieldMetadata collectionMetadata, Collection<Object> oldCollection, Collection<Object> newCollection) {
        super(collectionMetadata, () ->
                collectionMetadata.getType() == AUDIT_COLLECTION ?
                        auditEntityDiffIterator(collectionMetadata, oldCollection, newCollection) :
                        objectDiffIterator(oldCollection, newCollection));
    }

    private static Iterator<DiffPair> objectDiffIterator(Collection<Object> oldCollection, Collection<Object> newCollection) {
        var diffPairs = new ArrayList<DiffPair>();

        for (var oldEntity : oldCollection)
            if (!newCollection.contains(oldEntity))
                diffPairs.add(diffPair(oldEntity, null));

        for (var newEntity : newCollection)
            if (!oldCollection.contains(newEntity))
                diffPairs.add(diffPair(null, newEntity));

        return diffPairs.iterator();
    }

    private static Iterator<DiffPair> auditEntityDiffIterator(
            FieldMetadata collectionMetadata,
            Collection<Object> oldCollection,
            Collection<Object> newCollection) {

        // null заменяем на пустой сет
        if (oldCollection == null)
            oldCollection = Collections.emptySet();
        if (newCollection == null)
            newCollection = Collections.emptySet();

        var oldIdMap = oldCollection.stream()
                .collect(Collectors.toMap(
                        entity -> extractData(collectionMetadata.getEntityMetadataLink().getIdFieldMetadata(), entity),
                        entity -> entity));
        var newIdMap = newCollection.stream()
                .collect(Collectors.toMap(
                        entity -> extractData(collectionMetadata.getEntityMetadataLink().getIdFieldMetadata(), entity),
                        entity -> entity));

        var diffPairs = new ArrayList<DiffPair>();

        // доавляем элементы которых нет в новой коллекции (val -> null) и ищем пересечение
        for (var oldEntry : oldIdMap.entrySet())
            if (newIdMap.containsKey(oldEntry.getKey())) {
                var newEntity = newIdMap.get(oldEntry.getKey());

                // если эленты есть в обеих коллекциях и они не равны - значит они изменились, добавляем их тоже
                if (!Objects.equals(oldEntry.getValue(), newEntity))
                    diffPairs.add(diffPair(oldEntry.getValue(), newEntity));
            } else
                diffPairs.add(diffPair(oldEntry.getValue(), null));

        // искать пересечение большне не нужно, добавляем те которые появились (null -> val)
        for (var newEntry : newIdMap.entrySet())
            if (!oldIdMap.containsKey(newEntry.getKey()))
                diffPairs.add(diffPair(null, newEntry.getValue()));

        return diffPairs.iterator();
    }

    @Override
    protected State processAuditEntity(DiffPair diffPair) {
        var state = new ObjectDiffProcessing(
                collectionMetadata.getEntityMetadataLink().getIdFieldMetadata(),
                diffPair.oldEntity,
                diffPair.newEntity);

        state.setParamNamePrefix(updateName(String.valueOf(currentIterationNumber)));
        return state;
    }

    @Override
    protected void processObject(DiffPair diffPair) {
        addParam(String.valueOf(currentIterationNumber), getDiff(diffPair.oldEntity, diffPair.newEntity));
    }

    private static DiffPair diffPair(Object oldEntity, Object newEntity) {
        return new DiffPair(oldEntity, newEntity);
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
