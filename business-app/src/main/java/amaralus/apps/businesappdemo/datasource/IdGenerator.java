package amaralus.apps.businesappdemo.datasource;

import amaralus.apps.businesappdemo.datasource.models.AbstractModel;
import amaralus.apps.businesappdemo.datasource.models.CompositeKey;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Collectors;

// генератор не нужно делать бином, хибер сам создаст инстанс, а для соих нужд достаточно статики
public class IdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        if (!(object instanceof CompositeKey))
            throw new IllegalArgumentException("Неизвестный объект");

        return generate((CompositeKey) object);
    }

    // статический доступ по умолчанию для большего удобства использования
    public static <E extends AbstractModel<String> & CompositeKey> void generateId(E entity) {
        var id = generate(entity);
        entity.setId(id);
    }

    private static String generate(CompositeKey compositeKey) {
        var uniqueFields = compositeKey.getUniqueFields();

        if (uniqueFields == null || uniqueFields.isEmpty())
            throw new IllegalArgumentException("Уникальные поля не заданы");

        return String.valueOf(
                Objects.hash(
                        uniqueFields.stream()
                                .map(IdGenerator::getUniqueFieldValue)
                                .collect(Collectors.toList())));
    }

    private static Serializable getUniqueFieldValue(String uniqueField) {
        return Objects.requireNonNullElse(uniqueField, "null");
    }
}
