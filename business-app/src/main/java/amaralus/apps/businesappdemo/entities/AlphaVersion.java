package amaralus.apps.businesappdemo.entities;

import amaralus.apps.businesappdemo.infrastructure.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditEntity
public class AlphaVersion {
    private String versionValue;
    private String updateField;
}
