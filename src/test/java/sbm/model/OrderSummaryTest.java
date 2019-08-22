package sbm.model;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static sbm.TestUtils.givenOrderPairWithDifferentPrices;
import static sbm.TestUtils.givenOrderPairWithDifferentTypes;
import static sbm.TestUtils.givenOrderPairWithSameTypeAndPrice;
import static sbm.model.OrderType.SELL;

public class OrderSummaryTest {

    private OrderSummary target;

    @Test
    public void shouldSummarizeOrdersOfDifferentTypesSeparately() {
        //Given
        Order order1 = givenOrderPairWithDifferentTypes()[0];
        Order order2 = givenOrderPairWithDifferentTypes()[1];

        //When
        target = new OrderSummary(givenOrders(order1, order2));

        //Then
        Collection<Order> actual = target.getProcessedOrders();
        assertThat(actual, containsInAnyOrder(order1, order2));
    }

    @Test
    public void shouldSummarizeOrdersOfSameTypeDifferentPricesSeparately() {
        //Given
        Order order1 = givenOrderPairWithDifferentPrices(SELL)[0];
        Order order2 = givenOrderPairWithDifferentPrices(SELL)[1];

        //When
        target = new OrderSummary(givenOrders(order1, order2));

        //Then
        Collection<Order> actual = target.getProcessedOrders();
        assertThat(actual, containsInAnyOrder(order1, order2));
    }

    @Test
    public void shouldMergeOrdersOfSameTypeAndPriceRegardlessOfUsersInSummary() {
        //Given
        Order order1 = givenOrderPairWithSameTypeAndPrice()[0];
        Order order2 = givenOrderPairWithSameTypeAndPrice()[1];

        //When
        target = new OrderSummary(givenOrders(order1, order2));

        //Then
        final BigDecimal expectedQuantity = order1.getKgQuantity().add(order2.getKgQuantity());
        Order expected = Order.newBuilder()
                .withOrderType(order1.getOrderType())
                .withKgQuantity(expectedQuantity)
                .withUnitPrice(order1.getUnitPrice())
                .build();

        Collection<Order> actual = target.getProcessedOrders();
        assertThat(actual, hasSize(1));
        assertThat(actual, containsInAnyOrder(expected));
    }

    private Collection<Order> givenOrders(Order... orders) {
        return Arrays.asList(orders);
    }
}
