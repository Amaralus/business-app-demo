package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing;

import java.util.Objects;

public interface DiffProcessing {

    default String getDiff(Object oldValue, Object newValue) {
        if (Objects.equals(oldValue, newValue))
            return null;
        else
            return oldValue + " -> " + newValue;
    }
}
