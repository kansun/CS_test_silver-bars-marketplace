package sbm.exception;

import java.util.UUID;

public class DataValidationException extends RuntimeException {

    public static final String MISSING_ORDER = "Null input are not allowed.";
    public static final String MISSING_PRICE = "One or more orders missing unit price.";
    public static final String MISSING_TYPE = "One or more orders missing type (SELL/BUY?).";
    public static final String MISSING_QUANTITY = "One or more orders missing quantity in kg.";

    public DataValidationException(UUID errorId, String message) {
        super(String.format("{%s}: %s", errorId, message));
    }
}
