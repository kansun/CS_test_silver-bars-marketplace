package sbm.service.impl;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sbm.exception.DataValidationException;
import sbm.model.Order;
import sbm.service.OrderValidator;

import static java.math.BigDecimal.valueOf;
import static sbm.TestUtils.givenSingleOrder;
import static sbm.model.OrderType.SELL;

public class OrderValidatorImplTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
    private OrderValidator target = new OrderValidatorImpl();

    @Test
    public void shouldPassValidOrder() {
        //Given
        Order order = givenSingleOrder();

        //When
        target.validate(order);
    }

    @Test
    public void shouldThrowDataValidationExceptionWhenEitherOrderWithoutPrice() {
        expectedException.expect(DataValidationException.class);
        expectedException.expectMessage(DataValidationException.MISSING_PRICE);

        //Given
        Order order = Order.newBuilder().withOrderType(SELL).withKgQuantity(valueOf(1)).build();

        //When
        target.validate(order);
    }

    @Test
    public void shouldThrowDataValidationExceptionWhenEitherOrderWithoutType() {
        expectedException.expect(DataValidationException.class);
        expectedException.expectMessage(DataValidationException.MISSING_TYPE);

        //Given
        Order order = Order.newBuilder().withUnitPrice(valueOf(302)).withKgQuantity(valueOf(1)).build();

        //When
        target.validate(order);
    }

    @Test
    public void shouldThrowDataValidationExceptionWhenEitherOrderWithoutQuantity() {
        expectedException.expect(DataValidationException.class);
        expectedException.expectMessage(DataValidationException.MISSING_QUANTITY);

        //Given
        Order order = Order.newBuilder().withOrderType(SELL).withUnitPrice(valueOf(302)).build();

        //When
        target.validate(order);
    }

    @Test
    public void shouldThrowDataValidationExceptionWhenEitherOrderIsNull() {
        expectedException.expect(DataValidationException.class);
        expectedException.expectMessage(DataValidationException.MISSING_ORDER);

        //Given
        Order order = null;

        //When
        target.validate(order);
    }
}