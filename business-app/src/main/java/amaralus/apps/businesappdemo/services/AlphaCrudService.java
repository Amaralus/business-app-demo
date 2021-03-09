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
    private final AlphaMapper mapper;
    private final MergeMapper merger;

    public AlphaCrudService(AlphaRepository alphaRepository, AlphaMapper mapper, MergeMapper merger) {
        this.alphaRepository = alphaRepository;
        this.mapper = mapper;
        this.merger = merger;
    }

    @Transactional
    @Override
    public Alpha save(Alpha entity) {
        var model = mapper.alphaToModel(entity);
        return mapper.modelToAlpha(softSave(model));
    }

    private AlphaModel softSave(AlphaModel alphaModel){
        var founded = alphaRepository.getByIdIgnoreDeleted(alphaModel.getId());
        log.info("found ignore deleted");
        if (founded != null ) {
            log.info("preUpdate");
            merger.merge(alphaModel, founded);
            var saved = alphaRepository.save(founded);
            log.info("postUpdate");
            return saved;
        } else {
            log.info("preInsert");
            var saved = alphaRepository.save(alphaModel);
            log.info("postInsert");
            return saved;
        }
    }

    @Override
    public Alpha getById(String id) {
        return alphaRepository.findById(id)
                .map(mapper::modelToAlpha)
                .orElse(null);
    }

    @Transactional
    @Override
    public void delete(String id) {
        try {
            alphaRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ignored) {
            log.warn("alpha for deletion with id=[{}] not found", id);
        }
    }
}
