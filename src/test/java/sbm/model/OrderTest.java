package sbm.model;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static sbm.TestUtils.givenOrderPairWithDifferentPrices;
import static sbm.TestUtils.givenOrderPairWithDifferentTypes;
import static sbm.TestUtils.givenSingleOrder;
import static sbm.model.OrderType.BUY;
import static sbm.model.OrderType.SELL;

public class OrderTest {

    @Test
    public void shouldOrderEqualWhenTypePriceAndQuantityEqual() {
        //Given
        Order order1 = Order.newBuilder()
                .withOrderType(SELL)
                .withUnitPrice(valueOf(1))
                .withKgQuantity(valueOf(1))
                .withUserId("user1")
                .build();
        Order order2 = Order.newBuilder()
                .withOrderType(SELL)
                .withUnitPrice(valueOf(1))
                .withKgQuantity(valueOf(1))
                .withUserId("user2")
                .build();

        //Then
        assertThat(order1, equalTo(order2));
    }

    @Test
    public void shouldSellOrdersSortedByPriceAsc() {
        //Given
        Order order1 = givenOrderPairWithDifferentPrices(SELL)[0];
        Order order2 = givenOrderPairWithDifferentPrices(SELL)[1];

        //When
        int actual = order1.compareTo(order2);

        //Then
        BigDecimal price1 = order1.getUnitPrice();
        BigDecimal price2 = order2.getUnitPrice();

        MatcherAssert.assertThat(actual < 0, is(price1.compareTo(price2) < 0));
    }

    @Test
    public void shouldBuyOrdersSortedByPriceDesc() {
        //Given
        Order order1 = givenOrderPairWithDifferentPrices(BUY)[0];
        Order order2 = givenOrderPairWithDifferentPrices(BUY)[1];

        //When
        int actual = order1.compareTo(order2);

        //Then
        BigDecimal price1 = order1.getUnitPrice();
        BigDecimal price2 = order2.getUnitPrice();

        MatcherAssert.assertThat(actual < 0, is(price1.compareTo(price2) > 0));
    }

    //No acceptance criteria on this so taking assumption as SELL < BUY
    @Test
    public void shouldSellOrdersLessThanBuyRegardlessOfPrice() {
        //Given
        Order order1 = givenOrderPairWithDifferentTypes()[0];
        Order order2 = givenOrderPairWithDifferentTypes()[1];

        //When
        int actual = order1.compareTo(order2);

        //Then
        OrderType type1 = order1.getOrderType();
        OrderType type2 = order2.getOrderType();

        MatcherAssert.assertThat(actual < 0, is(type1 == SELL && type2 == BUY));
    }

    @Test
    public void shouldSinkNullToTheBottom() {
        //Given
        Order order1 = givenSingleOrder();
        Order order2 = null;

        //When
        int actual = order1.compareTo(order2);

        //Then
        MatcherAssert.assertThat(actual, is(lessThan(0)));
    }
}