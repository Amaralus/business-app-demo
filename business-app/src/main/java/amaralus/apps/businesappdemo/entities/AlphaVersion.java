package amaralus.apps.businesappdemo.entities;

import amaralus.apps.businesappdemo.infrastructure.audit.AuditEntity;
import amaralus.apps.businesappdemo.infrastructure.audit.AuditId;
import amaralus.apps.businesappdemo.infrastructure.audit.AuditParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditEntity(groupCode = "ADMINISTRATION")
public class AlphaVersion {
    @AuditId
    @AuditParam(name = "Значение версии", mandatory = true)
    @Pattern(regexp = "^\\d{2}\\.\\d{2}$")
    private String versionValue;
    @AuditParam(name = "Обновляемое поле")
    private String updateField;
}
