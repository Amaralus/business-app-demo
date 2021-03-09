package amaralus.apps.businesappdemo.services;

import amaralus.apps.businesappdemo.datasource.models.AlphaVersionModel;
import amaralus.apps.businesappdemo.datasource.repositories.AlphaRepository;
import amaralus.apps.businesappdemo.datasource.repositories.AlphaVersionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashSet;

import static amaralus.apps.businesappdemo.TestUtil.alpha;
import static amaralus.apps.businesappdemo.TestUtil.alphaVersion;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest
@TestPropertySource("classpath:persistence-db.properties")
@TestInstance(PER_CLASS)
@Slf4j
@DisplayName("Тесты для сущности AlphaVersion")
class AlphaVersionModelsCrudTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    AlphaCrudService alphaCrudService;
    @Autowired
    AlphaRepository alphaRepository;
    @Autowired
    AlphaVersionRepository alphaVersionRepository;

    @Test
    @DisplayName("Сохранение Alpha и AlphaVersion | В базе пусто")
    @Transactional
    void saveVersion() {
        var alpha = alpha("code1", alphaVersion());

        var result = alphaCrudService.save(alpha);

        assertEquals(alpha, result);
        assertTrue(alphaRepository.existsById(alpha.getCode()));

        var versions = entityManager.createQuery("select v from AlphaVersionModel v where v.alphaCode=:code and v.deleted=false ",
                AlphaVersionModel.class)
                .setParameter("code", alpha.getCode())
                .getResultList();
        assertNotNull(versions);
        assertEquals(1, versions.size());
    }

    @Test
    @DisplayName("Сохранение Alpha и AlphaVersion | В базе есть записи")
    @Transactional
    void saveVersionUpdate() {

        var set = new HashSet<>();

        var alpha = alpha("code1", alphaVersion());

        alphaCrudService.save(alpha);
        alpha.setUpdateField("update");
        alpha.getVersion().setUpdateField("update");
        var result = alphaCrudService.save(alpha);

        assertEquals(alpha, result);
        assertTrue(alphaRepository.existsById(alpha.getCode()));
    }

    // сохранение в базе обе записи удалены
    // сохранение в базе есть альфа с другой версией (перезатирание?)
    // сохранение в базе есть альфа с другой версией (добавление?)
}
