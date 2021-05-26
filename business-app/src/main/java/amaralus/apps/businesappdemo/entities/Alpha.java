package amaralus.apps.businesappdemo.entities;

import amaralus.apps.businesappdemo.infrastructure.audit.AuditEntity;
import amaralus.apps.businesappdemo.infrastructure.audit.AuditId;
import amaralus.apps.businesappdemo.infrastructure.audit.AuditParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditEntity(groupCode = "ADMINISTRATION")
public class Alpha {

    @AuditId
    @AuditParam(name = "Код")
    private String code;
    @AuditParam(name = "Обновляемое поле")
    private String updateField;
    @AuditParam(name = "Версия")
    private AlphaVersion version;
    @AuditParam(name = "Набор тэт")
    private Set<String> thetas;
}
