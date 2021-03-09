package amaralus.apps.businesappdemo.datasource.repositories;

import amaralus.apps.businesappdemo.datasource.models.AlphaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlphaRepository extends JpaRepository<AlphaModel, String> {

    @Query("select a from AlphaModel a where a.alphaCode=:id")
    AlphaModel getByIdIgnoreDeleted(String id);
}
