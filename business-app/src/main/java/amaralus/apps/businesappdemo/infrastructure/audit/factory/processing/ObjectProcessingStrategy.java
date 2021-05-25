package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing;

import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;

public class ObjectProcessingStrategy extends FieldProcessingStrategy {

    public Object process(FieldMetadata fieldMetadata, Object entity) {
        return extractData(fieldMetadata, entity);
    }
}
