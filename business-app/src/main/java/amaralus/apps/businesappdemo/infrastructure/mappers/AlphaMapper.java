package amaralus.apps.businesappdemo.infrastructure.mappers;

import amaralus.apps.businesappdemo.datasource.IdGenerator;
import amaralus.apps.businesappdemo.datasource.models.AlphaModel;
import amaralus.apps.businesappdemo.datasource.models.AlphaVersionModel;
import amaralus.apps.businesappdemo.entities.Alpha;
import amaralus.apps.businesappdemo.entities.AlphaVersion;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface AlphaMapper {

    IdGenerator idGenerator = new IdGenerator();

    @Mapping(target = "code", source = "alphaCode")
    @Mapping(target = "version", source = "alphaVersionModels")
    Alpha modelToAlpha(AlphaModel alphaModel);

    @Mapping(source = "code", target = "alphaCode")
    @Mapping(source = "version", target = "alphaVersionModels")
    AlphaModel alphaToModel(Alpha alpha);

    AlphaVersion modelToAlphaVersion(AlphaVersionModel alphaVersionModel);

    @Mapping(target = "versionId", ignore = true)
    @Mapping(target = "alphaCode", ignore = true)
    @Mapping(target = "alphaModel", ignore = true)
    AlphaVersionModel alphaVersionToModel(AlphaVersion alphaVersion);

    default List<AlphaVersionModel> alphaVersionToModelList(AlphaVersion alphaVersion) {
        if (alphaVersion == null)
            return Collections.emptyList();

        return List.of(alphaVersionToModel(alphaVersion));
    }

    default AlphaVersion getFirstVersion(List<AlphaVersionModel> versionModelList) {
        if (isEmpty(versionModelList))
            return null;

        return modelToAlphaVersion(versionModelList.get(0));
    }

    @AfterMapping
    default void alphaModelAfterMapping(@MappingTarget AlphaModel alphaModel) {
        for (var versionModel : alphaModel.getAlphaVersionModels()) {
            versionModel.setAlphaCode(alphaModel.getAlphaCode());
            versionModel.setId(idGenerator.generate(versionModel));
        }
    }

    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "rowVersion", ignore = true)
    @Mapping(target = "alphaVersionModels", ignore = true)
    void mergeModel(AlphaModel source, @MappingTarget AlphaModel target);
}
