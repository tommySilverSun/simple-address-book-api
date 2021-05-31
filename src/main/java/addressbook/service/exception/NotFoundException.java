package addressbook.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * This exception is thrown when required resource cannot be found. It is bind to {@link HttpStatus#NOT_FOUND}
 * So, the spring framework will auto respond the caller with 404 error code
 *
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }
}
