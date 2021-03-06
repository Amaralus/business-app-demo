package amaralus.apps.businesappdemo.services;

import amaralus.apps.businesappdemo.datasource.models.ThetaModel;
import amaralus.apps.businesappdemo.datasource.repositories.ThetaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
// Сохранение у тет отличается от обычных моделей,
// потому имеет смысл сделать отедльный контракт без привязки к общему интерфейсу
// сохраняются только сами теты, линки сохраняются через объект к которому они привязываются
public class ThetaCrudService {

    private final ThetaRepository thetaRepository;

    public ThetaCrudService(ThetaRepository thetaRepository) {
        this.thetaRepository = thetaRepository;
    }

    @Transactional
    public Set<ThetaModel> save(Set<String> thetas) {
        var result = new HashSet<ThetaModel>();
        for (var code : thetas)
            result.add(save(code));
        return result;
    }

    @Transactional
    public ThetaModel save(String code) {
        log.info(" Сохранение Theta code=[{}]", code);
        var theta = thetaRepository.getByCodeIgnoreDeleted(code);

        if (theta == null)
            theta = thetaRepository.save(new ThetaModel(UUID.randomUUID(), code));
        theta.restore();
        return theta;
    }
}
