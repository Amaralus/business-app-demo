package amaralus.apps.businesappdemo.infrastructure.audit.metadata;

import amaralus.apps.businesappdemo.infrastructure.audit.AuditEntity;
import amaralus.apps.businesappdemo.infrastructure.audit.AuditId;
import amaralus.apps.businesappdemo.infrastructure.audit.AuditParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

@Slf4j
public class AuditEntityValidator {

    private final Class<?> entityClass;

    private int paramsCount;
    private int idCount;
    private boolean lowPerformance;
    private int walkDepth;

    public static boolean isValidEntity(Class<?> entityClass) {
        return new AuditEntityValidator(entityClass).validate();
    }

    private AuditEntityValidator(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    private boolean validate() {
        extractValidationInfo();

        if (paramsCount == 0) {
            log.debug("Entity [{}] must contains at least one audit parameter!", entityClass.getName());
            return false;
        }

        if (lowPerformance)
            log.warn("Entity [{}] have low performance to data extraction for audit event. Debug for more details", entityClass.getName());

        if (idCount != 1) {
            log.debug("Entity [{}] must contains only 1 id-filed!", entityClass.getName());
            return false;
        }

        if (walkDepth < 0) {
            log.debug("walk depth for entity [{}] must be greeter then 0!", entityClass.getName());
            return false;
        }

        return true;
    }

    private void extractValidationInfo() {
        walkDepth = entityClass.getAnnotation(AuditEntity.class).walkDepth();

        for (var field : entityClass.getDeclaredFields()) {
            if (field.getAnnotation(AuditParam.class) != null) ++paramsCount;

            var idField = field.getAnnotation(AuditId.class) != null;

            if (idField) ++idCount;

            var getter = BeanUtils.getPropertyDescriptor(entityClass, field.getName()).getReadMethod();
            if (getter == null) {
                lowPerformance = true;
                log.debug("Getter must exist for field [{}] to improve performance", field.getName());
            }
        }
    }
}
