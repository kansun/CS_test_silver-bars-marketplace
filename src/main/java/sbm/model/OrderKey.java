package sbm.model;

import java.math.BigDecimal;
import java.util.Objects;

import static sbm.model.OrderType.SELL;

public class OrderKey implements Comparable<OrderKey> {
    private final OrderType orderType;
    private final BigDecimal unitPrice;

    public OrderKey(OrderType orderType, BigDecimal unitPrice) {
        this.orderType = orderType;
        this.unitPrice = unitPrice;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderKey that = (OrderKey) o;
        return getOrderType() == that.getOrderType() &&
                Objects.equals(getUnitPrice(), that.getUnitPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderType(), getUnitPrice());
    }

    @Override
    public int compareTo(OrderKey o) {
        if (this.getOrderType() != o.getOrderType()) {
            return this.getOrderType() == SELL ? -1 : 1;
        } else {
            return getOrderType() == SELL ?
                    this.getUnitPrice().compareTo(o.getUnitPrice()) : o.getUnitPrice().compareTo(this.getUnitPrice());
        }
    }
}
