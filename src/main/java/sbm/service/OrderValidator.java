package sbm.service;

import sbm.model.Order;

public interface OrderValidator {
    void validate(Order order);
}
