package amaralus.apps.businesappdemo.datasource.repositories;

import amaralus.apps.businesappdemo.datasource.models.AlphaVersionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlphaVersionRepository extends JpaRepository<AlphaVersionModel, String> {
}
