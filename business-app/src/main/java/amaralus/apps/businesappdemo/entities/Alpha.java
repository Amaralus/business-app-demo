package amaralus.apps.businesappdemo.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Alpha {

    private String code;
    private String updateField;
    private AlphaVersion version;
    private Set<String> thetas;
}
