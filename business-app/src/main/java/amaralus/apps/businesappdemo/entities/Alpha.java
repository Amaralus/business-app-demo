package amaralus.apps.businesappdemo.entities;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Alpha {

    private String code;
    private List<AlphaVersion> versions;
}
