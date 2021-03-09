package amaralus.apps.businesappdemo.datasource.repositories;

import amaralus.apps.businesappdemo.datasource.models.AlphaVersionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlphaVersionRepository extends JpaRepository<AlphaVersionModel, String> {

    @Query("select a from AlphaVersionModel a where a.versionId=:id")
    AlphaVersionModel getByIdIgnoreDeleted(String id);
}
