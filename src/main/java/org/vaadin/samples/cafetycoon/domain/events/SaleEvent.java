package org.vaadin.samples.cafetycoon.domain.events;

import org.vaadin.samples.cafetycoon.domain.BaseDomainEvent;
import org.vaadin.samples.cafetycoon.domain.Cafe;
import org.vaadin.samples.cafetycoon.domain.CoffeeDrink;

import java.math.BigDecimal;

/**
 * In a real world, you would want to be able to buy more than one type of drink at the same time, but
 * this is good enough for this sample application.
 */
public class SaleEvent extends BaseDomainEvent {

    private final Cafe cafe;
    private final CoffeeDrink drink;
    private final int quantity;

    public SaleEvent(Cafe cafe, CoffeeDrink drink, int quantity) {
        this.cafe = cafe;
        this.drink = drink;
        this.quantity = quantity;
    }

    /**
     * The café where the sale took place.
     */
    public Cafe getCafe() {
        return cafe;
    }

    /**
     * The drink that was sold.
     */
    public CoffeeDrink getDrink() {
        return drink;
    }

    /**
     * The number of drinks that were sold (min. 1).
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * The total number of bean units that were consumed when the drinks of this sale were made.
     */
    public BigDecimal getConsumedBeanUnits() {
        return drink.getBeanUnits().multiply(new BigDecimal(quantity));
    }

    /**
     * The total amount of money that the café got in return for the drinks of this sale.
     */
    public BigDecimal getTotalIncome() {
        return drink.getPrice().multiply(new BigDecimal(quantity));
    }

    @Override
    public String toString() {
        return String.format("SaleEvent{cafe=%s, drink=%s, quantity=%d}", cafe, drink, quantity);
    }
}
