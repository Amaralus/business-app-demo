package amaralus.apps.businesappdemo.infrastructure.mappers;

import amaralus.apps.businesappdemo.datasource.models.AlphaModel;
import amaralus.apps.businesappdemo.datasource.models.AlphaVersionModel;
import amaralus.apps.businesappdemo.entities.Alpha;
import amaralus.apps.businesappdemo.entities.AlphaVersion;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.springframework.util.StringUtils.isEmpty;

@Mapper(componentModel = "spring")
public interface AlphaMapper {

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
    default void afterMapping(AlphaModel alphaModel) {
        for (var versionModel : alphaModel.getAlphaVersionModels()) {
            versionModel.setAlphaCode(alphaModel.getAlphaCode());
            versionModel.setVersionId(
                    String.valueOf(Objects.hash(
                            versionModel.getVersionValue(),
                            versionModel.getAlphaCode())));
        }
    }
}
