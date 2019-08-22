package sbm.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static sbm.model.OrderType.BUY;
import static sbm.model.OrderType.SELL;

public class OrderKeyTest {

    @Test
    public void shouldEqualOnSameTypeAndPrice() {
        //Given
        OrderKey key1 = new OrderKey(SELL, BigDecimal.valueOf(1.23));
        OrderKey key2 = new OrderKey(SELL, BigDecimal.valueOf(1.23));

        //Then
        assertThat(key1, equalTo(key2));
    }

    @Test
    public void shouldDifferOnDifferentType() {
        //Given
        OrderKey key1 = new OrderKey(SELL, BigDecimal.valueOf(1.23));
        OrderKey key2 = new OrderKey(BUY, BigDecimal.valueOf(1.23));

        //Then
        assertThat(key1, not(equalTo(key2)));
    }

    @Test
    public void shouldDifferOnDifferentPrice() {
        //Given
        OrderKey key1 = new OrderKey(SELL, BigDecimal.valueOf(1.23));
        OrderKey key2 = new OrderKey(SELL, BigDecimal.valueOf(2.34));

        //Then
        assertThat(key1, not(equalTo(key2)));
    }

    @Test
    public void shouldSellBeLessThanBuy() {
        //Given
        OrderKey key1 = new OrderKey(SELL, BigDecimal.valueOf(1.23));
        OrderKey key2 = new OrderKey(BUY, BigDecimal.valueOf(0.23));

        //Then
        assertThat(key1.compareTo(key2), lessThan(0));
    }

    @Test
    public void shouldLowerPriceOnTopForSell() {
        //Given
        OrderKey key1 = new OrderKey(SELL, BigDecimal.valueOf(1.23));
        OrderKey key2 = new OrderKey(SELL, BigDecimal.valueOf(0.23));

        //Then
        assertThat(key1.compareTo(key2), greaterThan(0));
    }

    @Test
    public void shouldLowerPriceOnTopForBuy() {
        //Given
        OrderKey key1 = new OrderKey(BUY, BigDecimal.valueOf(1.23));
        OrderKey key2 = new OrderKey(BUY, BigDecimal.valueOf(0.23));

        //Then
        assertThat(key1.compareTo(key2), lessThan(0));
    }
}