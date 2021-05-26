package amaralus.apps.businesappdemo.entities;

import amaralus.apps.businesappdemo.infrastructure.audit.AuditEntity;
import amaralus.apps.businesappdemo.infrastructure.audit.AuditId;
import amaralus.apps.businesappdemo.infrastructure.audit.AuditParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditEntity(groupCode = "ADMINISTRATION")
public class AlphaVersion {
    @AuditId
    @AuditParam(name = "Значение версии")
    private String versionValue;
    @AuditParam(name = "Обновляемое поле")
    private String updateField;
}
