package addressbook.service.addressbook.contactvalidator;

/**
 * Contain result for a Contact validation process
 *
 */
public class ContactValidatorResult {

    private final Boolean isValid;

    // If the Contact is not valid. A exception is expected here
    private final RuntimeException exception;

    public ContactValidatorResult(Boolean isValid, RuntimeException exception) {
        this.isValid = isValid;
        this.exception = exception;
    }

    public Boolean getValid() {
        return isValid;
    }


    public RuntimeException getException() {
        return exception;
    }
}
