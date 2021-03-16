package amaralus.apps.businesappdemo.datasource;

import amaralus.apps.businesappdemo.datasource.models.CompositeKey;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class IdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return generate(object);
    }

    public String generate(Object object) {
        if (!(object instanceof CompositeKey))
            throw new IllegalArgumentException("Неизвестный объект");

        var uniqueFields = ((CompositeKey) object).getUniqueFields();

        if (uniqueFields == null || uniqueFields.isEmpty())
            throw new IllegalArgumentException("Уникальные поля не заданы");

        return String.valueOf(
                Objects.hash(
                        uniqueFields.stream()
                                .map(this::getUniqueFieldValue)
                                .collect(Collectors.toList())));
    }

    private Serializable getUniqueFieldValue(String uniqueField) {
        return Objects.requireNonNullElse(uniqueField, "null");
    }
}
