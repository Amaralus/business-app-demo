package amaralus.apps.businesappdemo.datasource.repositories;

import amaralus.apps.businesappdemo.datasource.models.AlphaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlphaRepository extends JpaRepository<AlphaModel, String> {
}
