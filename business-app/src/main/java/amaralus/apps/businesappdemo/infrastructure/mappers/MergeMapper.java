package amaralus.apps.businesappdemo.infrastructure.mappers;

import amaralus.apps.businesappdemo.datasource.models.AbstractModel;
import amaralus.apps.businesappdemo.datasource.models.AlphaModel;
import amaralus.apps.businesappdemo.datasource.models.AlphaVersionModel;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface MergeMapper {

    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "rowVersion", ignore = true)
    @Mapping(target = "alphaVersionModels", qualifiedByName = "versionsAddMapping")
    void merge(AlphaModel source, @MappingTarget AlphaModel target);

    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "rowVersion", ignore = true)
    @Mapping(target = "uniqueFields", ignore = true)
    @Mapping(target = "alphaModel", ignore = true)
    void merge(AlphaVersionModel source, @MappingTarget AlphaVersionModel target);

    @Named("versionsRetainMapping")
    default void mergeRetain(List<AlphaVersionModel> source, @MappingTarget List<AlphaVersionModel> target) {
        var sourceIds = source.stream()
                .map(AbstractModel::getId)
                .collect(Collectors.toSet());

        target.removeIf(model -> !sourceIds.contains(model.getId()));
        var targetMap = target.stream().collect(Collectors.toMap(AbstractModel::getId, model -> model));

        for (var sourceVersion : source)
            merge(sourceVersion, targetMap.get(sourceVersion.getId()));
    }

    @Named("versionsAddMapping")
    default void mergeAdd(List<AlphaVersionModel> source, @MappingTarget List<AlphaVersionModel> target) {
        var targetMap = target.stream().collect(Collectors.toMap(AbstractModel::getId, model -> model));

        for (var sourceVersion : source)
            if (targetMap.containsKey(sourceVersion.getId()))
                merge(sourceVersion, targetMap.get(sourceVersion.getId()));
            else
                target.add(sourceVersion);

        target.removeIf(AbstractModel::isDeleted);
    }
}
