package sbm.service;

import sbm.model.Order;

import java.util.Collection;

public interface LiveOrderBoard {
    void register(Order order);

    void remove(Order order);

    Collection<Order> getSummary();
}
