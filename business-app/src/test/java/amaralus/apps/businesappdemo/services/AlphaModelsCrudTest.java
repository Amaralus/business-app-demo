package amaralus.apps.businesappdemo.services;

import amaralus.apps.businesappdemo.datasource.repositories.AlphaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static amaralus.apps.businesappdemo.TestUtil.alpha;
import static amaralus.apps.businesappdemo.TestUtil.alphaVersion;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest
@TestPropertySource("classpath:persistence-db.properties")
@TestInstance(PER_CLASS)
@Slf4j
class AlphaModelsCrudTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    AlphaCrudService alphaCrudService;
    @Autowired
    AlphaRepository alphaRepository;

    // Alpha only

    @Test
    @DisplayName("Сохранение Alpha | В базе пусто")
    @Transactional
    void saveAlpha() {
        var alpha = alpha("code1");

        var result = alphaCrudService.save(alpha);

        assertEquals(alpha, result);
        assertTrue(alphaRepository.existsById(alpha.getCode()));
    }

    @Test
    @DisplayName("Обновление Alpha | В базе есть запись")
    @Transactional
    void updateAlpha() {
        var alpha = alpha("code1");

        alphaCrudService.save(alpha);
        alpha.setUpdateField("update");
        var result = alphaCrudService.save(alpha);

        assertEquals(alpha, result);
        assertTrue(alphaRepository.existsById(alpha.getCode()));
    }

    @Test
    @DisplayName("Удаление Alpha | В базе есть запись")
    @Transactional
    void deleteAlpha() {
        var alpha = alpha("code1");

        alphaCrudService.save(alpha);
        alphaCrudService.delete(alpha.getCode());

        assertTrue(alphaRepository.existsById(alpha.getCode()));
        var alphaModel = alphaRepository.getByIdIgnoreDeleted(alpha.getCode());
        assertTrue(alphaModel.isDeleted());
    }

    @Test
    @DisplayName("Сохранение Alpha | В базе есть удаленная запись")
    @Transactional
    void saveDeletedAlpha() {
        var alpha = alpha("code1");

        alphaCrudService.save(alpha);
        alphaCrudService.delete(alpha.getCode());
        var result = alphaCrudService.save(alpha);

        assertEquals(alpha, result);
        assertTrue(alphaRepository.existsById(alpha.getCode()));
    }

    // Alpha And AlphaVersion

    @Test
    @DisplayName("Сохранение Alpha и AlphaVersion | В базе пусто")
    @Transactional
    void saveAlphaWithVersion() {
        var alpha = alpha("code1", alphaVersion("0.1"));

        var result = alphaCrudService.save(alpha);

        assertEquals(alpha, result);
        assertTrue(alphaRepository.existsById(alpha.getCode()));
    }
}