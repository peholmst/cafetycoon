package org.vaadin.samples.cafetycoon.domain;

import com.google.common.eventbus.EventBus;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Services implements ServletContextListener {

    private static Services INSTANCE;

    private EventBus eventBus;

    private CafeRepository cafeRepository;
    private CoffeeDrinkRepository coffeeDrinkRepository;

    private StockService stockService;
    private CafeStatusService cafeStatusService;

    public EventBus getEventBus() {
        return eventBus;
    }

    public CafeRepository getCafeRepository() {
        return cafeRepository;
    }

    public StockService getStockService() {
        return stockService;
    }

    public CoffeeDrinkRepository getCoffeeDrinkRepository() {
        return coffeeDrinkRepository;
    }

    public CafeStatusService getCafeStatusService() {
        return cafeStatusService;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        eventBus = new EventBus();
        cafeRepository = new CafeRepository();
        stockService = new StockService(eventBus);
        coffeeDrinkRepository = new CoffeeDrinkRepository();
        cafeStatusService = new CafeStatusService(eventBus, coffeeDrinkRepository, cafeRepository, stockService);
        INSTANCE = this;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    public static Services getInstance() {
        return INSTANCE;
    }
}
