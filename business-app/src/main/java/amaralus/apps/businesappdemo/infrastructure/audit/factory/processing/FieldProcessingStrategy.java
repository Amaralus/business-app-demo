package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing;

import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class FieldProcessingStrategy {

    protected Object extractData(FieldMetadata fieldMetadata, Object targetObject) {
        try {
            if (fieldMetadata.getGetterMethod() != null)
                return fieldMetadata.getGetterMethod().invoke(targetObject);
            else {
                var field = fieldMetadata.getEntityClass().getDeclaredField(fieldMetadata.getName());
                field.setAccessible(true);
                return field.get(targetObject);
            }
        } catch (Exception e) {
            log.warn("Unsuccessful data extraction from field [" + fieldMetadata.getName() + "]");
            return "DATA EXTRACTION ERROR";
        }
    }
}
