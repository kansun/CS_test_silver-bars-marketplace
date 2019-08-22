package sbm;

import sbm.model.Order;
import sbm.model.OrderType;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static sbm.model.OrderType.BUY;
import static sbm.model.OrderType.SELL;

public interface TestUtils {

    static Order givenSingleOrder() {
        return Order.newBuilder()
                .withOrderType(SELL)
                .withKgQuantity(valueOf(3.5))
                .withUnitPrice(valueOf(306))
                .withUserId("user1")
                .build();
    }

    static Order[] givenOrderPairWithDifferentTypes() {
        Order order1 = Order.newBuilder()
                .withOrderType(SELL)
                .withKgQuantity(valueOf(3.5))
                .withUnitPrice(valueOf(306))
                .withUserId("user1")
                .build();
        Order order2 = Order.newBuilder(order1)
                .withOrderType(BUY)
                .build();
        return new Order[]{order1, order2};
    }

    static Order[] givenOrderPairWithDifferentPrices(OrderType orderType) {
        Order order1 = Order.newBuilder()
                .withOrderType(orderType)
                .withKgQuantity(valueOf(3.5))
                .withUnitPrice(valueOf(306))
                .withUserId("user1")
                .build();
        Order order2 = Order.newBuilder(order1)
                .withUnitPrice(valueOf(303))
                .build();
        return new Order[]{order1, order2};
    }

    static Order[] givenOrderPairWithSameTypeAndPrice() {
        Order order1 = Order.newBuilder()
                .withOrderType(SELL)
                .withKgQuantity(BigDecimal.valueOf(3.5))
                .withUnitPrice(BigDecimal.valueOf(305))
                .withUserId("user1")
                .build();
        Order order2 = Order.newBuilder(order1)
                .withKgQuantity(BigDecimal.valueOf(2.0))
                .withUserId("user2")
                .build();
        return new Order[]{order1, order2};
    }
}
