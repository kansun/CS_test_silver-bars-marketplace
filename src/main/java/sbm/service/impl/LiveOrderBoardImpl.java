package sbm.service.impl;

import sbm.model.Order;
import sbm.model.OrderSummary;
import sbm.service.LiveOrderBoard;
import sbm.service.OrderValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LiveOrderBoardImpl implements LiveOrderBoard {

    private final List<Order> orders;
    private final OrderValidator orderValidator;

    public LiveOrderBoardImpl(OrderValidator orderValidator) {
        this.orderValidator = orderValidator;
        this.orders = new ArrayList<>();
    }

    @Override
    public void register(Order order) {
        orderValidator.validate(order);
        orders.add(order);
    }

    @Override
    public void remove(Order order) {
        orderValidator.validate(order);
        orders.remove(order);
    }

    @Override
    public Collection<Order> getSummary() {
        return new OrderSummary(orders).getProcessedOrders();

    }
}
