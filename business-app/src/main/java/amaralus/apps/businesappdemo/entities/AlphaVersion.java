package amaralus.apps.businesappdemo.entities;

import amaralus.apps.businesappdemo.infrastructure.audit.AuditEntity;
import amaralus.apps.businesappdemo.infrastructure.audit.AuditId;
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
    private String versionValue;
    private String updateField;
}
