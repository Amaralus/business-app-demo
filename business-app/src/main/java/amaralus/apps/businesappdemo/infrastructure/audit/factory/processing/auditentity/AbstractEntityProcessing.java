package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.auditentity;

import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.IterableProcessing;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.State;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.collection.AbstractCollectionProcessing;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.object.ObjectProcessing;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;

import java.util.Iterator;
import java.util.function.Supplier;

public abstract class AbstractEntityProcessing extends IterableProcessing<FieldMetadata> {

    protected final int walkDepth;
    protected boolean useParentNamePrefix;

    protected AbstractEntityProcessing(Supplier<Iterator<FieldMetadata>> iteratorSupplier, int walkDepth) {
        super(iteratorSupplier);
        this.walkDepth = walkDepth;
    }

    @Override
    protected State getNextState(FieldMetadata fieldMetadata) {
        switch (fieldMetadata.getType()) {
            case AUDIT_ENTITY:
                if (walkDepth == 0)
                    return null;
                return auditEntityStrategy(fieldMetadata);
            case COLLECTION:
            case AUDIT_COLLECTION:
                return collectionProcessingStrategy(fieldMetadata);
            default:
                return objectStrategy(fieldMetadata);
        }
    }

    protected State objectStrategy(FieldMetadata fieldMetadata) {
        var strategy = newObjectProcessing(fieldMetadata);
        if (useParentNamePrefix)
            strategy.setParamNamePrefix(paramNamePrefix);
        return strategy;
    }

    protected abstract ObjectProcessing newObjectProcessing(FieldMetadata fieldMetadata);

    protected State auditEntityStrategy(FieldMetadata fieldMetadata) {
        var strategy = newAuditEntityProcessing(fieldMetadata);
        strategy.setUseParentNamePrefix(true);
        strategy.setParamNamePrefix(updateName(fieldMetadata.getParamName()));
        return strategy;
    }

    protected abstract AbstractEntityProcessing newAuditEntityProcessing(FieldMetadata fieldMetadata);

    protected State collectionProcessingStrategy(FieldMetadata fieldMetadata) {
        var strategy = newCollectionProcessing(fieldMetadata);
        strategy.setParamNamePrefix(updateName(fieldMetadata.getParamName()));
        return strategy;
    }

    protected abstract AbstractCollectionProcessing newCollectionProcessing(FieldMetadata fieldMetadata);

    public void setUseParentNamePrefix(boolean useParentNamePrefix) {
        this.useParentNamePrefix = useParentNamePrefix;
    }
}
