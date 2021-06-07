package amaralus.apps.businesappdemo.infrastructure.mappers;

import amaralus.apps.businesappdemo.datasource.IdGenerator;
import amaralus.apps.businesappdemo.datasource.models.AlphaModel;
import amaralus.apps.businesappdemo.datasource.models.AlphaVersionModel;
import amaralus.apps.businesappdemo.datasource.models.ThetaModel;
import amaralus.apps.businesappdemo.entities.Alpha;
import amaralus.apps.businesappdemo.entities.AlphaVersion;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface AlphaMapper {

    @Mapping(target = "code", source = "alphaCode")
    @Mapping(target = "version", source = "alphaVersionModels")
    Alpha modelToAlpha(AlphaModel alphaModel);

    @Mapping(source = "code", target = "alphaCode")
    @Mapping(source = "version", target = "alphaVersionModels")
    @Mapping(target = "thetas", ignore = true)
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

    default Set<String> thetaModelToString(Set<ThetaModel> thetaModels) {
        return thetaModels.stream()
                .map(ThetaModel::getThetaCode)
                .collect(Collectors.toSet());
    }

    // генерация айдишников на уровне маппера
    @AfterMapping
    default void alphaModelAfterMapping(@MappingTarget AlphaModel alphaModel) {
        for (var versionModel : alphaModel.getAlphaVersionModels()) {
            versionModel.setAlphaCode(alphaModel.getAlphaCode());
            IdGenerator.generateId(versionModel);
        }
    }
}
