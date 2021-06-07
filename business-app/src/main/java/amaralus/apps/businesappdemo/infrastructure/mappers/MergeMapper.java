package amaralus.apps.businesappdemo.infrastructure.mappers;

import amaralus.apps.businesappdemo.datasource.models.AbstractModel;
import amaralus.apps.businesappdemo.datasource.models.AlphaModel;
import amaralus.apps.businesappdemo.datasource.models.AlphaVersionModel;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Мерджер - специальный маппер задача которого копипаста данных одной сущности в другую такую же.
 * Для каждной сущности нужно создавать отделльный маппер но все главные методы мерджа надо назвать merge.
 */
@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface MergeMapper {

    // все что нам не надо копировать просто игнорируем
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "rowVersion", ignore = true)
    @Mapping(target = "thetaLinks", ignore = true)
    @Mapping(target = "thetas", ignore = true)
    // для вложеных сущностий используем специальынй маппинг
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

        // убираем из таргета все модели которых нет в сурсе
        target.removeIf(model -> !sourceIds.contains(model.getId()));
        mergeAdd(source, target);
    }

    @Named("versionsAddMapping")
    default void mergeAdd(List<AlphaVersionModel> source, @MappingTarget List<AlphaVersionModel> target) {
        // мапа айдишник - модель
        var targetMap = target.stream().collect(Collectors.toMap(AbstractModel::getId, model -> model));

        for (var sourceVersion : source)
            // если модель уже есть в таргете, то просто мерджим иначе добавляем новую
            // это нужно чтобы модели только добавлялись в итоговую коллекцию
            // и модифицировать модели которые уже есть в контексте хибера
            if (targetMap.containsKey(sourceVersion.getId()))
                merge(sourceVersion, targetMap.get(sourceVersion.getId()));
            else
                target.add(sourceVersion);

        // убираем удаленные чтобы потом они немешали, на всякий случай если нужна дополнительная обработка
        target.removeIf(AbstractModel::isDeleted);
    }

    @AfterMapping
    default void afterAlphaModel(AlphaModel source, @MappingTarget AlphaModel target) {
        target.setThetas(source.getThetas());
    }
}
