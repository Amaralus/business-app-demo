package amaralus.apps.businesappdemo.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static amaralus.apps.businesappdemo.TestUtil.alpha;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

@SpringBootTest
@TestPropertySource("classpath:persistence-db.properties")
@TestInstance(PER_CLASS)
@Execution(CONCURRENT)
@DisplayName("Тесты для сущности AlphaThetaLinkModel")
class AlphaThetaLinkModelTest {

    @Autowired
    AlphaCrudService alphaCrudService;

    @Test
    @DisplayName("Сохранение Alpha и Theta | В базе пусто")
    @Transactional(isolation = SERIALIZABLE)
    void save() {
        var alpha = alpha();
        alpha.setThetas(Set.of("save"));

        alphaCrudService.save(alpha);
        var result = alphaCrudService.getById(alpha.getCode());

        assertEquals(alpha, result);
    }

    @Test
    @DisplayName("Сохранение Alpha и Theta | В базе есть записи")
    @Transactional(isolation = SERIALIZABLE)
    void replace() {
        var alpha = alpha();
        alpha.setThetas(Set.of("insert"));

        alphaCrudService.save(alpha);
        alpha.setThetas(Set.of("update"));
        alphaCrudService.save(alpha);

        var result = alphaCrudService.getById(alpha.getCode());

        assertEquals(alpha, result);
    }

    @Test
    @DisplayName("Сохранение Alpha и Theta | В базе есть удаленная запись")
    @Transactional(isolation = SERIALIZABLE)
    void restore() {
        var alpha = alpha();
        alpha.setThetas(Set.of("restore"));

        alphaCrudService.save(alpha);
        alphaCrudService.delete(alpha.getCode());
        alphaCrudService.save(alpha);

        var result = alphaCrudService.getById(alpha.getCode());

        assertEquals(alpha, result);
    }
}
