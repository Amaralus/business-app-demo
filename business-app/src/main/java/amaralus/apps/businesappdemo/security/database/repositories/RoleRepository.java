package amaralus.apps.businesappdemo.security.database.repositories;

import amaralus.apps.businesappdemo.security.database.models.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleModel, String> {
}
