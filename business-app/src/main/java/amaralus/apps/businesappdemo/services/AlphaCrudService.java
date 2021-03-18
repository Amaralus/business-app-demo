package amaralus.apps.businesappdemo.services;

import amaralus.apps.businesappdemo.datasource.models.AlphaModel;
import amaralus.apps.businesappdemo.datasource.repositories.AlphaRepository;
import amaralus.apps.businesappdemo.entities.Alpha;
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

    public AlphaCrudService(AlphaRepository alphaRepository, ThetaCrudService thetaCrudService, AlphaMapper mapper, MergeMapper merger) {
        this.alphaRepository = alphaRepository;
        this.thetaCrudService = thetaCrudService;
        this.mapper = mapper;
        this.merger = merger;
    }

    @Transactional
    @Override
    public Alpha save(Alpha alpha) {
        log.info("Сохранение Alpha code=[{}] version=[{}]", alpha.getCode(), alpha.getVersion() == null ? null : alpha.getVersion().getVersionValue());
        var model = mapper.alphaToModel(alpha);

        var thetas = thetaCrudService.save(alpha.getThetas());
        model.setThetas(thetas);

        var result = softSave(model);
        return mapper.modelToAlpha(result.clearDeleted());
    }

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
                .map(alphaModel -> mapper.modelToAlpha(alphaModel.clearDeleted()))
                .orElse(null);
    }

    @Transactional
    @Override
    public void delete(String id) {
        log.info("delete alpha id=[{}]", id);
        try {
            alphaRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ignored) {
            log.warn("alpha for deletion with id=[{}] not found", id);
        }
    }
}
