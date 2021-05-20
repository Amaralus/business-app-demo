package amaralus.apps.businesappdemo.infrastructure.audit.metadata;

import amaralus.apps.businesappdemo.infrastructure.audit.AuditExclude;
import amaralus.apps.businesappdemo.infrastructure.audit.AuditId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

@Slf4j
public class EntityValidator {

    private final Class<?> entityClass;

    private int idCount;
    private boolean idExcluded;
    private boolean lowPerformance;

    public static boolean isValidEntity(Class<?> entityClass) {
        return new EntityValidator(entityClass).validate();
    }

    private EntityValidator(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    private boolean validate() {
        extractValidationInfo();

        if (lowPerformance)
            log.warn("Entity [{}] have low performance to data extraction for audit event. Debug for more details", entityClass.getName());

        if (idCount != 1) {
            log.debug("Entity [{}] must contains only 1 id-filed!", entityClass.getName());
            return false;
        }

        if (idExcluded) {
            log.debug("Id-field can't be excluded!");
            return false;
        }

        return true;
    }

    private void extractValidationInfo() {
        for (var field : entityClass.getDeclaredFields()) {
            var exclude = field.getAnnotation(AuditExclude.class) != null;
            var idField = field.getAnnotation(AuditId.class) != null;

            if(idField) ++idCount;
            idExcluded = idField && exclude;

            var getter = BeanUtils.getPropertyDescriptor(entityClass, field.getName()).getReadMethod();
            if (getter == null) {
                lowPerformance = true;
                log.debug("Getter must exist for field [{}] to improve performance", field.getName());
            }
        }
    }
}
