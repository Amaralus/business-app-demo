package amaralus.apps.businesappdemo.services;

import amaralus.apps.businesappdemo.datasource.models.AlphaModel;
import amaralus.apps.businesappdemo.datasource.repositories.AlphaRepository;
import amaralus.apps.businesappdemo.entities.Alpha;
import amaralus.apps.businesappdemo.infrastructure.mappers.AlphaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AlphaCrudService implements CrudService<Alpha, String> {

    private final AlphaRepository alphaRepository;
    private final AlphaMapper mapper;

    public AlphaCrudService(AlphaRepository alphaRepository, AlphaMapper mapper) {
        this.alphaRepository = alphaRepository;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public Alpha save(Alpha entity) {
        var model = mapper.alphaToModel(entity);
        return mapper.modelToAlpha(softSave(model));
    }

    private AlphaModel softSave(AlphaModel alphaModel){
        var founded = alphaRepository.getByIdIgnoreDeleted(alphaModel.getId());
        if (founded != null ) {
            mapper.mergeModel(alphaModel, founded);
            return alphaRepository.save(founded);
        } else {
            return alphaRepository.save(alphaModel);
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
        alphaRepository.deleteById(id);
    }
}
