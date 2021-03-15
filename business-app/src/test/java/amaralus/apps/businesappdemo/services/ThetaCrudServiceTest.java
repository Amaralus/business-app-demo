package amaralus.apps.businesappdemo.services;

import amaralus.apps.businesappdemo.datasource.repositories.ThetaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest
@TestPropertySource("classpath:persistence-db.properties")
@TestInstance(PER_CLASS)
@DisplayName("Тесты сервиса сущностей Theta")
class ThetaCrudServiceTest {

    private static final String THETA_CODE = "theta";

    @Autowired
    ThetaCrudService thetaCrudService;
    @Autowired
    ThetaRepository thetaRepository;

    @Test
    @DisplayName("Сохранение Theta | В базе пусто")
    @Transactional
    void save() {
        var result = thetaCrudService.save(THETA_CODE);

        assertNotNull(result);
        assertNotNull(result.getThetaId());
        assertEquals(THETA_CODE, result.getThetaCode());
    }

    @Test
    @DisplayName("Сохранение Theta | В базе есть записть")
    @Transactional
    void update() {
        thetaCrudService.save(THETA_CODE);
        var result = thetaCrudService.save(THETA_CODE);

        assertNotNull(result);
        assertNotNull(result.getThetaId());
        assertEquals(THETA_CODE, result.getThetaCode());
    }

    @Test
    @DisplayName("Сохранение Theta | В базе есть удаленная записть")
    @Transactional
    void restore() {
        var result = thetaCrudService.save(THETA_CODE);
        thetaRepository.delete(result);
        result = thetaCrudService.save(THETA_CODE);

        assertNotNull(result);
        assertFalse(result.isDeleted());
        assertNotNull(result.getThetaId());
        assertEquals(THETA_CODE, result.getThetaCode());

        var founded = thetaRepository.getByCodeIgnoreDeleted(THETA_CODE);
        assertEquals(result, founded);
    }
}