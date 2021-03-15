package amaralus.apps.businesappdemo.datasource.repositories;

import amaralus.apps.businesappdemo.datasource.models.ThetaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ThetaRepository extends JpaRepository<ThetaModel, UUID> {

    @Query("select t from ThetaModel t where t.thetaCode=:code")
    ThetaModel getByCodeIgnoreDeleted(String code);
}
