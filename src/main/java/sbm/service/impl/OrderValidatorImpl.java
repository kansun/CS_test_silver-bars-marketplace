package sbm.service.impl;

import sbm.exception.DataValidationException;
import sbm.model.Order;
import sbm.service.OrderValidator;

import static java.util.UUID.randomUUID;
import static sbm.exception.DataValidationException.MISSING_ORDER;
import static sbm.exception.DataValidationException.MISSING_PRICE;
import static sbm.exception.DataValidationException.MISSING_QUANTITY;
import static sbm.exception.DataValidationException.MISSING_TYPE;

public class OrderValidatorImpl implements OrderValidator {
    @Override
    public void validate(Order order) {
        if (order == null) {
            throw new DataValidationException(randomUUID(), MISSING_ORDER);
        }
        if (order.getUnitPrice() == null) {
            throw new DataValidationException(randomUUID(), MISSING_PRICE);
        }
        if (order.getOrderType() == null) {
            throw new DataValidationException(randomUUID(), MISSING_TYPE);
        }
        if (order.getKgQuantity() == null) {
            throw new DataValidationException(randomUUID(), MISSING_QUANTITY);
        }
    }
}
