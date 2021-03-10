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
        var version = getByCodeAndVersion(alpha.getCode(), alpha.getVersion().getVersionValue());
        assertNotNull(version);
    }

    @Test
    @DisplayName("Сохранение Alpha и AlphaVersion | В базе есть записи")
    @Transactional
    void saveVersionUpdate() {
        var alpha = alpha("code1", alphaVersion());

        alphaCrudService.save(alpha);
        alpha.setUpdateField("update");
        alpha.getVersion().setUpdateField("update");
        var result = alphaCrudService.save(alpha);

        assertEquals(alpha, result);
        var version = getByCodeAndVersion(alpha.getCode(), alpha.getVersion().getVersionValue());
        assertNotNull(version);
    }

    @Test
    @DisplayName("Сохранение Alpha и AlphaVersion | В базе есть удаленные записи")
    @Transactional
    void saveVersionRestoreDeleted() {
        var alpha = alpha("code1", alphaVersion());

        alphaCrudService.save(alpha);
        alphaCrudService.delete(alpha.getCode());
        var result = alphaCrudService.save(alpha);

        assertEquals(alpha, result);
        var version = getByCodeAndVersion(alpha.getCode(), alpha.getVersion().getVersionValue());
        assertNotNull(version);
        assertFalse(version.isDeleted());
    }

    @Test
    @DisplayName("Сохранение Alpha и AlphaVersion | В базе есть другие записи")
    @Transactional
    void saveVersionContainsOtherVersion() {
        var alpha = alpha("code1", alphaVersion());

        alphaCrudService.save(alpha);
        alpha.setVersion(alphaVersion("0.2"));
        var result = alphaCrudService.save(alpha);

        assertEquals(alpha, result);
        var model = alphaRepository.findById(alpha.getCode());
        assertTrue(model.isPresent());
        assertFalse(model.get().getAlphaVersionModels().isEmpty());
        assertEquals(2, model.get().getAlphaVersionModels().size());
    }

    @Test
    @DisplayName("Сохранение Alpha и AlphaVersion | В базе есть другие удаленные записи")
    @Transactional
    void saveVersionContainsOtherDeletedVersion() {
        var alpha = alpha("code1", alphaVersion());

        alphaCrudService.save(alpha);
        alphaCrudService.delete(alpha.getCode());
        alpha.setVersion(alphaVersion("0.2"));
        var result = alphaCrudService.save(alpha);

        assertEquals(alpha, result);
        var model = alphaRepository.findById(alpha.getCode());
        assertTrue(model.isPresent());
        var versions = model.get().getAlphaVersionModels();
        assertFalse(versions.isEmpty());
        assertEquals(1, versions.size());
        assertEquals(alpha.getVersion().getVersionValue(), versions.get(0).getVersionValue());

    }

    @Test
    @DisplayName("Удаление Alpha и AlphaVersion | В базе есть записи")
    @Transactional
    void delete() {
        var alpha = alpha("code1", alphaVersion());

        alphaCrudService.save(alpha);
        alphaCrudService.delete(alpha.getCode());

        var version = getByCodeAndVersion(alpha.getCode(), alpha.getVersion().getVersionValue());
        assertNotNull(version);
        assertTrue(version.isDeleted());
    }

    @Test
    @DisplayName("Получение Alpha и AlphaVersion | В базе несколько версий")
    @Transactional
    void getSeveralVersion() {
        var alpha = alpha("code1", alphaVersion());

        alphaCrudService.save(alpha);
        alpha.setVersion(alphaVersion("0.2"));
        alphaCrudService.save(alpha);

        var result = alphaCrudService.getById(alpha.getCode());
        assertEquals(alpha, result);
    }

    private AlphaVersionModel getByCodeAndVersion(String code, String version) {
        return entityManager.createQuery("select v from AlphaVersionModel v where v.alphaCode=:code and v.versionValue=:version",
                AlphaVersionModel.class)
                .setParameter("code", code)
                .setParameter("version", version)
                .getSingleResult();
    }
}
