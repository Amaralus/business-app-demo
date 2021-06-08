package amaralus.apps.businesappdemo.services;

import amaralus.apps.businesappdemo.datasource.models.AlphaModel;
import amaralus.apps.businesappdemo.datasource.repositories.AlphaRepository;
import amaralus.apps.businesappdemo.entities.Alpha;
import amaralus.apps.businesappdemo.infrastructure.audit.AuditService;
import amaralus.apps.businesappdemo.infrastructure.audit.EventType;
import amaralus.apps.businesappdemo.infrastructure.mappers.AlphaMapper;
import amaralus.apps.businesappdemo.infrastructure.mappers.MergeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AlphaCrudService implements CrudService<Alpha, String> {

    private final AlphaRepository alphaRepository;
    private final ThetaCrudService thetaCrudService;
    private final AlphaMapper mapper;
    private final MergeMapper merger;
    private final AuditService auditService;

    public AlphaCrudService(AlphaRepository alphaRepository, ThetaCrudService thetaCrudService, AlphaMapper mapper, MergeMapper merger, AuditService auditService) {
        this.alphaRepository = alphaRepository;
        this.thetaCrudService = thetaCrudService;
        this.mapper = mapper;
        this.merger = merger;
        this.auditService = auditService;
    }

    @Transactional
    @Override
    public Alpha save(Alpha alpha) {
        log.info("Сохранение Alpha code=[{}] version=[{}]", alpha.getCode(), alpha.getVersion() == null ? null : alpha.getVersion().getVersionValue());
        var model = mapper.alphaToModel(alpha);
        var old = getById(alpha.getCode());

        // так как теты приходят в виде сета строк, то сохраняем их отедельно в специальном методе
        // и получаем назад сет моделей для сохраения линков
        var thetas = thetaCrudService.save(alpha.getThetas());
        model.setThetas(thetas);

        var result = softSave(model);
        var saved = mapper.modelToAlpha(result.clearDeleted());
        auditService.successEvent(EventType.SAVE)
                .entity(alpha, old)
                .send();
        return saved;
    }

    // основное предназначение этого мтеода найти модель если она уже была
    // и обновить ее и все вложенные модели через мерджер
    // после этого сохрается обновленная модель так как она уже числится в контексте хибера
    // что позволяет упростить сохранение и избежать ошибок
    private AlphaModel softSave(AlphaModel alphaModel){
        var founded = alphaRepository.getByIdIgnoreDeleted(alphaModel.getId());
        if (founded != null ) {
            merger.merge(alphaModel, founded);
            return alphaRepository.save(founded);
        } else {
            return alphaRepository.save(alphaModel);
        }
    }

    @Override
    public Alpha getById(String id) {
        return alphaRepository.findById(id)
                // так как вложеные сущности подтягиваются без учета флага удаления, то удаленные сущности нужно убрать
                .map(alphaModel -> mapper.modelToAlpha(alphaModel.clearDeleted()))
                .orElse(null);
    }

    @Transactional
    @Override
    public void delete(String id) {
        log.info("Удаление Alpha code=[{}]", id);
        try {
            auditService.successEvent(EventType.DELETE)
                    .entity(getById(id))
                    .send();
            alphaRepository.deleteById(id);
            // метод генерирует экспеш когда нет записи в базе, потому его просто игнорируем
        } catch (EmptyResultDataAccessException ignored) {
            log.warn("Модель Aplha code=[{}] для удаления не найдена!", id);
        }
    }
}
