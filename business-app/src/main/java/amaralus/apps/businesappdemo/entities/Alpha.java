package amaralus.apps.businesappdemo.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Alpha {

    private String code;
    private AlphaVersion version;
}
