package sbm.model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import static java.math.BigDecimal.ZERO;
import static java.util.stream.Collectors.toList;

public final class OrderSummary {

    private final Map<OrderKey, BigDecimal> summary;

    public OrderSummary(Collection<Order> orders) {
        summary = new HashMap<>();
        orders.forEach(order -> {
            BigDecimal quantity = summary.get(order.getOrderKey());
            BigDecimal updated = null == quantity ? order.getKgQuantity() : quantity.add(order.getKgQuantity());
            summary.put(order.getOrderKey(), updated);
        });
    }

    public Collection<Order> getProcessedOrders() {
        List<Order> orders = summary.entrySet().stream().map(e ->
                Order.newBuilder()
                        .withOrderType(e.getKey().getOrderType())
                        .withUnitPrice(e.getKey().getUnitPrice())
                        .withKgQuantity(null == e.getValue() ? ZERO : e.getValue()).build()
        ).collect(toList());
        return new TreeSet<>(orders);
    }
}