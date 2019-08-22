package sbm.model;

import java.math.BigDecimal;
import java.util.Objects;

public final class Order implements Comparable<Order> {
    private final String userId;
    private final BigDecimal kgQuantity;
    private final OrderKey orderKey;

    private Order(Builder builder) {
        userId = builder.userId;
        kgQuantity = builder.kgQuantity;
        orderKey = new OrderKey(builder.orderType, builder.unitPrice);
    }

    public static Builder newBuilder(Order copy) {
        Builder builder = newBuilder();
        builder.userId = copy.getUserId();
        builder.kgQuantity = copy.getKgQuantity();
        builder.unitPrice = copy.getUnitPrice();
        builder.orderType = copy.getOrderType();
        return builder;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getUserId() {
        return userId;
    }

    public BigDecimal getKgQuantity() {
        return kgQuantity;
    }

    public BigDecimal getUnitPrice() {
        return orderKey.getUnitPrice();
    }

    public OrderType getOrderType() {
        return orderKey.getOrderType();
    }

    public OrderKey getOrderKey() {
        return orderKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(getKgQuantity(), order.getKgQuantity()) &&
                Objects.equals(getOrderKey(), order.getOrderKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKgQuantity(), getOrderKey());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("userId='").append(userId).append('\'');
        sb.append(", kgQuantity=").append(kgQuantity);
        sb.append(", unitPrice=").append(getUnitPrice());
        sb.append(", orderType=").append(getOrderType());
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(Order o) {
        if (null == o) {
            return -1;
        }
        int keyComparision = this.getOrderKey().compareTo(o.getOrderKey());
        return keyComparision;
    }

    public static final class Builder {
        private String userId;
        private BigDecimal kgQuantity;
        private BigDecimal unitPrice;
        private OrderType orderType;

        private Builder() {
        }

        public Builder withUserId(String val) {
            userId = val;
            return this;
        }

        public Builder withKgQuantity(BigDecimal val) {
            kgQuantity = val;
            return this;
        }

        public Builder withUnitPrice(BigDecimal val) {
            unitPrice = val;
            return this;
        }

        public Builder withOrderType(OrderType val) {
            orderType = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}