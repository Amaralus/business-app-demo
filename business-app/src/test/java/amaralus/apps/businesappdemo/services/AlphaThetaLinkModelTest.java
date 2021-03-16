package amaralus.apps.businesappdemo.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static amaralus.apps.businesappdemo.TestUtil.alpha;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest
@TestPropertySource("classpath:persistence-db.properties")
@TestInstance(PER_CLASS)
@DisplayName("Тесты для сущности AlphaThetaLinkModel")
class AlphaThetaLinkModelTest {

    @Autowired
    AlphaCrudService alphaCrudService;

    @Test
    @Transactional
    void save() {
        var alpha = alpha();
        alpha.setThetas(Set.of("theta"));

        alphaCrudService.save(alpha);
        var result = alphaCrudService.getById(alpha.getCode());

        assertEquals(alpha, result);
    }
}
