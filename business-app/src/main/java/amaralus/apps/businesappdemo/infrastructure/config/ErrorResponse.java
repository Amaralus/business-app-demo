package amaralus.apps.businesappdemo.infrastructure.config;

import com.google.common.base.Throwables;
import lombok.Getter;

import java.util.UUID;

import static java.util.UUID.randomUUID;

@Getter
public class ErrorResponse {

    private final String message;
    private final String cause;
    private final UUID errorId;

    public ErrorResponse(String message, Exception e) {
        this.message = message;
        cause = Throwables.getRootCause(e).getMessage();
        errorId = randomUUID();
    }
}
