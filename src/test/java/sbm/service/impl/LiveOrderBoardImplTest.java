package sbm.service.impl;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sbm.model.Order;
import sbm.model.OrderSummary;
import sbm.service.LiveOrderBoard;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.math.BigDecimal.valueOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static sbm.TestUtils.givenOrderPairWithDifferentPrices;
import static sbm.TestUtils.givenOrderPairWithDifferentTypes;
import static sbm.TestUtils.givenOrderPairWithSameTypeAndPrice;
import static sbm.TestUtils.givenSingleOrder;
import static sbm.model.OrderType.BUY;
import static sbm.model.OrderType.SELL;

public class LiveOrderBoardImplTest {

    private LiveOrderBoard target;
    private boolean validatorInvoked;

    @Before
    public void setUp() {
        target = new LiveOrderBoardImpl(order -> validatorInvoked = true);
    }

    @After
    public void tearDown() {
        validatorInvoked = false;
    }

    @Test
    public void shouldRegisterSingleOrderAsItIs() {
        //Given
        Order order = givenSingleOrder();

        //When
        target.register(order);

        //Then
        Collection<Order> actual = target.getSummary();
        assertThat(actual, contains(order));
    }

    @Test
    public void shouldSummarizeOrdersOfDifferentTypesSeparately() {
        //Given
        Order order1 = givenOrderPairWithDifferentTypes()[0];
        Order order2 = givenOrderPairWithDifferentTypes()[1];

        //When
        target.register(order1);
        target.register(order2);

        //Then
        Collection<Order> actual = target.getSummary();
        assertThat(actual, containsInAnyOrder(order1, order2));
    }

    @Test
    public void shouldSummarizeOrdersOfSameTypeDifferentPricesSeparately() {
        //Given
        Order order1 = givenOrderPairWithDifferentPrices(SELL)[0];
        Order order2 = givenOrderPairWithDifferentPrices(SELL)[1];

        //When
        target.register(order1);
        target.register(order2);

        //Then
        Collection<Order> actual = target.getSummary();
        assertThat(actual, containsInAnyOrder(order1, order2));
    }

    @Test
    public void shouldRemoveOrderWhenMatching() {
        //Given
        Order order1 = givenOrderPairWithSameTypeAndPrice()[0];
        Order order2 = givenOrderPairWithSameTypeAndPrice()[1];
        target.register(order1);
        target.register(order2);

        //When
        target.remove(order2);

        //Then
        Collection<Order> actual = target.getSummary();
        assertThat(actual, contains(order1));
    }

    @Test
    public void shouldMergeOrdersOfSameTypeAndPriceRegardlessOfUsersInSummary() {
        //Given
        Order order1 = givenOrderPairWithSameTypeAndPrice()[0];
        Order order2 = givenOrderPairWithSameTypeAndPrice()[1];

        //When
        target.register(order1);
        target.register(order2);

        //Then
        final BigDecimal expectedQuantity = order1.getKgQuantity().add(order2.getKgQuantity());
        Order expected = Order.newBuilder()
                .withOrderType(order1.getOrderType())
                .withKgQuantity(expectedQuantity)
                .withUnitPrice(order1.getUnitPrice())
                .build();

        Collection<Order> actual = target.getSummary();
        assertThat(actual, hasSize(1));
        assertThat(actual, containsInAnyOrder(expected));
    }

    @Test
    public void shouldSellOrdersSortedByPriceAsc() {
        //Given
        Order order1 = givenOrderPairWithDifferentPrices(SELL)[0];
        Order order2 = givenOrderPairWithDifferentPrices(SELL)[1];

        //When
        target.register(order1);
        target.register(order2);

        //Then
        Collection<Order> actual = target.getSummary();
        BigDecimal price1 = order1.getUnitPrice();
        BigDecimal price2 = order2.getUnitPrice();
        Matcher<Iterable<? extends Order>> orderAssertion =
                price1.compareTo(price2) < 0 ? contains(order1, order2) : contains(order2, order1);
        assertThat(actual, orderAssertion);
    }

    @Test
    public void shouldBuyOrdersSortedByPriceDesc() {
        //Given
        Order order1 = givenOrderPairWithDifferentPrices(BUY)[0];
        Order order2 = givenOrderPairWithDifferentPrices(BUY)[1];

        //When
        target.register(order1);
        target.register(order2);

        //Then
        Collection<Order> actual = target.getSummary();
        BigDecimal price1 = order1.getUnitPrice();
        BigDecimal price2 = order2.getUnitPrice();
        Matcher<Iterable<? extends Order>> orderAssertion =
                price1.compareTo(price2) > 0 ? contains(order1, order2) : contains(order2, order1);
        assertThat(actual, orderAssertion);
    }

    @Test
    public void shouldInvokeValidatorWhenRegisteringOrder() {
        //Given
        target.register(givenSingleOrder());

        //Then
        assertThat(validatorInvoked, is(true));
    }

    @Test
    public void shouldInvokeValidatorWhenRemovingOrder() {
        //Given
        target.remove(givenSingleOrder());

        //Then
        assertThat(validatorInvoked, is(true));
    }

    @Test
    public void shouldReturnSummaryInCorrectOrderWhenMixedOrderInput() {
        //Given
        /**
         - a) SELL: 3.5 kg for £306 [user1]
         - b) SELL: 1.2 kg for £310 [user2]
         - c) SELL: 1.5 kg for £307 [user3]
         - d) SELL: 2.0 kg for £306 [user4]

         - e) BUY: 3.5 kg for £306 [user1]
         - f) BUY: 1.2 kg for £310 [user2]
         - g) BUY: 1.5 kg for £307 [user3]
         - h) BUY: 2.0 kg for £306 [user4]
         */
        Order order1 = Order.newBuilder().withOrderType(SELL).withKgQuantity(valueOf(3.5)).withUnitPrice(valueOf(306)).withUserId("user1").build();
        Order order2 = Order.newBuilder().withOrderType(SELL).withKgQuantity(valueOf(1.2)).withUnitPrice(valueOf(310)).withUserId("user2").build();
        Order order3 = Order.newBuilder().withOrderType(SELL).withKgQuantity(valueOf(1.5)).withUnitPrice(valueOf(307)).withUserId("user3").build();
        Order order4 = Order.newBuilder().withOrderType(SELL).withKgQuantity(valueOf(2.0)).withUnitPrice(valueOf(306)).withUserId("user4").build();

        Order order5 = Order.newBuilder().withOrderType(BUY).withKgQuantity(valueOf(3.5)).withUnitPrice(valueOf(306)).withUserId("user1").build();
        Order order6 = Order.newBuilder().withOrderType(BUY).withKgQuantity(valueOf(1.2)).withUnitPrice(valueOf(310)).withUserId("user2").build();
        Order order7 = Order.newBuilder().withOrderType(BUY).withKgQuantity(valueOf(1.5)).withUnitPrice(valueOf(307)).withUserId("user3").build();
        Order order8 = Order.newBuilder().withOrderType(BUY).withKgQuantity(valueOf(2.0)).withUnitPrice(valueOf(306)).withUserId("user4").build();

        List<Order> orderInput = Arrays.asList(order1, order2, order3, order4, order5, order6, order7, order8);

        //When
        orderInput.forEach(order -> target.register(order));

        //Then
        Collection<Order> actual = target.getSummary();
        Order mergedSell = Order.newBuilder().withOrderType(SELL).withUnitPrice(valueOf(306)).withKgQuantity(valueOf(5.5)).build();
        Order mergedBuy = Order.newBuilder().withOrderType(BUY).withUnitPrice(valueOf(306)).withKgQuantity(valueOf(5.5)).build();
        assertThat(actual, contains(
                mergedSell, order3, order2, order6, order7, mergedBuy
        ));

    }
}