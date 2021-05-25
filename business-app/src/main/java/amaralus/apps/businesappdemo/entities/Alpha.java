package amaralus.apps.businesappdemo.entities;

import amaralus.apps.businesappdemo.infrastructure.audit.AuditEntity;
import amaralus.apps.businesappdemo.infrastructure.audit.AuditId;
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
    private String code;
    private String updateField;
    private AlphaVersion version;
    private Set<String> thetas;
}
