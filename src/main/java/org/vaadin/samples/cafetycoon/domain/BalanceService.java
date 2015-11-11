package org.vaadin.samples.cafetycoon.domain;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;

public class BalanceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BalanceService.class);
    private static final BigDecimal BEAN_UNIT_PRICE = new BigDecimal("0.50");

    private final Map<Cafe, BigDecimal> balances = new HashMap<>();

    public BalanceService(EventBus eventBus) {
        eventBus.register(this);
    }

    @Subscribe
    protected synchronized void onSaleEvent(SaleEvent event) {
        updateBalance(event.getCafe(), event.getTotalIncome());
    }

    @Subscribe
    protected synchronized void onRestockEvent(RestockEvent event) {
        updateBalance(event.getCafe(), event.getBeanUnits().multiply(BEAN_UNIT_PRICE).negate());
    }

    private void updateBalance(Cafe cafe, BigDecimal difference) {
        BigDecimal oldBalance = balances.getOrDefault(cafe, BigDecimal.ZERO);
        BigDecimal newBalance = oldBalance.add(difference);
        balances.put(cafe, newBalance);
        LOGGER.info("Total balance of {} is now {}", cafe, newBalance);
    }

    public synchronized BigDecimal getCurrentBalance(Cafe cafe) {
        return balances.getOrDefault(cafe, BigDecimal.ZERO);
    }

}
