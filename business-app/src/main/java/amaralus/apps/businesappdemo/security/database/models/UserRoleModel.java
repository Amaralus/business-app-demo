package amaralus.apps.businesappdemo.security.database.models;

import amaralus.apps.businesappdemo.security.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "user_role")
@Data
public class UserRoleModel {

    @EmbeddedId
    private Id id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "username", insertable = false, updatable = false, nullable = false,
            foreignKey = @ForeignKey(name = "fk_userrole_username_user_username"))
    private UserModel userModel;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "role_name", insertable = false, updatable = false, nullable = false,
            foreignKey = @ForeignKey(name = "fk_userrole_rolename_role_name"))
    private RoleModel roleModel;

    public UserRoleModel() {}

    public UserRoleModel(String username, Role roleName) {
        id = new Id(username, roleName);
    }

    @Embeddable
    @Data
    public static class Id implements Serializable {

        private String username;
        @Column(name = "role_name")
        @Enumerated(STRING)
        private Role roleName;

        public Id() {}

        public Id(String username, Role roleName) {
            this.username = username;
            this.roleName = roleName;
        }
    }
}
