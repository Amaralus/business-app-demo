package amaralus.apps.businesappdemo.security.database.repositories;

import amaralus.apps.businesappdemo.security.database.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, String> {
}
