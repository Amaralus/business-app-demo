package amaralus.apps.businesappdemo.services;

import amaralus.apps.businesappdemo.datasource.repositories.AlphaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static amaralus.apps.businesappdemo.TestUtil.alpha;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

@SpringBootTest
@TestPropertySource("classpath:persistence-db.properties")
@TestInstance(PER_CLASS)
@Execution(CONCURRENT)
@DisplayName("Тесты для сущности Alpha")
class AlphaModelsCrudTest {

    @Autowired
    AlphaCrudService alphaCrudService;
    @Autowired
    AlphaRepository alphaRepository;

    @Test
    @DisplayName("Сохранение Alpha | В базе пусто")
    @Transactional
    void saveAlpha() {
        var alpha = alpha();

        var result = alphaCrudService.save(alpha);

        assertEquals(alpha, result);
        assertTrue(alphaRepository.existsById(alpha.getCode()));
    }

    @Test
    @DisplayName("Сохранение Alpha | В базе есть запись")
    @Transactional
    void updateAlpha() {
        var alpha = alpha();

        alphaCrudService.save(alpha);
        alpha.setUpdateField("update");
        var result = alphaCrudService.save(alpha);

        assertEquals(alpha, result);
        assertTrue(alphaRepository.existsById(alpha.getCode()));
    }

    @Test
    @DisplayName("Сохранение Alpha | В базе есть удаленная запись")
    @Transactional(isolation = SERIALIZABLE)
    void saveDeletedAlpha() {
        var alpha = alpha();

        alphaCrudService.save(alpha);
        alphaCrudService.delete(alpha.getCode());
        var result = alphaCrudService.save(alpha);

        assertEquals(alpha, result);
        assertTrue(alphaRepository.existsById(alpha.getCode()));
    }

    @Test
    @DisplayName("Удаление Alpha | В базе есть запись")
    @Transactional(isolation = SERIALIZABLE)
    void deleteAlpha() {
        var alpha = alpha();

        alphaCrudService.save(alpha);
        alphaCrudService.delete(alpha.getCode());

        var alphaModel = alphaRepository.getByIdIgnoreDeleted(alpha.getCode());
        assertTrue(alphaModel.isDeleted());
    }

    @Test
    @DisplayName("Удаление Alpha | В базе нет записи")
    @Transactional(isolation = SERIALIZABLE)
    void deleteAlphaNoRow() {
        alphaCrudService.delete("none");

        assertFalse(alphaRepository.existsById("none"));
    }

    @Test
    @DisplayName("Удаление Alpha | В базе есть удаленная запись")
    @Transactional(isolation = SERIALIZABLE)
    void deleteAlphaDeletedRow() {
        var alpha = alpha();

        alphaCrudService.save(alpha);
        alphaCrudService.delete(alpha.getCode());
        alphaCrudService.delete(alpha.getCode());

        assertTrue(alphaRepository.existsById(alpha.getCode()));
        var alphaModel = alphaRepository.getByIdIgnoreDeleted(alpha.getCode());
        assertTrue(alphaModel.isDeleted());
    }

    @Test
    @DisplayName("Получение Alpha | В базе есть запись")
    @Transactional(isolation = SERIALIZABLE)
    void getAlpha() {
        var alpha = alpha();

        alphaCrudService.save(alpha);
        var result = alphaCrudService.getById(alpha.getCode());

        assertEquals(alpha, result);
    }

    @Test
    @DisplayName("Получение Alpha | В базе пусто")
    @Transactional(isolation = SERIALIZABLE)
    void getAlphaNoRow() {
        var result = alphaCrudService.getById("none");

        assertNull(result);
    }

    @Test
    @DisplayName("Получение Alpha | В базе есть удаленная запись")
    @Transactional(isolation = SERIALIZABLE)
    void getAlphaDeletedRow() {
        var alpha = alpha();

        alphaCrudService.save(alpha);
        alphaCrudService.delete(alpha.getCode());
        var result = alphaCrudService.getById(alpha.getCode());

        assertNull(result);
    }
}