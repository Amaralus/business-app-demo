package amaralus.apps.businesappdemo.security.database.models;

import amaralus.apps.businesappdemo.security.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.ALL;
import static org.hibernate.annotations.LazyCollectionOption.TRUE;

@Entity
@Table(name = "user_t")
@Data
public class UserModel implements UserDetails {

    @Id
    private String username;
    private String password;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "userModel", cascade = ALL, orphanRemoval = true)
    @LazyCollection(TRUE)
    private Set<UserRoleModel> userRoleModels = new HashSet<>();

    public void addRole(Role role) {
        userRoleModels.add(new UserRoleModel(username, role));
    }

    public Set<RoleModel> getRoleModels() {
        return userRoleModels.stream()
                .map(UserRoleModel::getRoleModel)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoleModels();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;

    }
}
