package amaralus.apps.businesappdemo.security.database.models;

import amaralus.apps.businesappdemo.security.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleModel implements GrantedAuthority {

    @Id
    @Enumerated(STRING)
    private Role name;

    @Override
    public String getAuthority() {
        return getName().name();
    }
}
