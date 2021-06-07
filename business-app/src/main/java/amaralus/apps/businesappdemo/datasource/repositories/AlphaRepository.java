package amaralus.apps.businesappdemo.datasource.repositories;

import amaralus.apps.businesappdemo.datasource.models.AlphaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// репозитории достаточно создать только для тех сущностей, которые нужно запрашивать непосредственно
// все дочерние модели будут получены через основные
public interface AlphaRepository extends JpaRepository<AlphaModel, String> {

    // в каждом репозитории должен быть метод получения модели без учета флага удаления
    @Query("select a from AlphaModel a where a.alphaCode=:id")
    AlphaModel getByIdIgnoreDeleted(String id);
}
