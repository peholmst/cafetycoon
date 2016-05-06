package org.vaadin.samples.cafetycoon.domain;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.google.common.eventbus.EventBus;

import java.math.BigDecimal;

@WebListener
public class Services implements ServletContextListener {

    private static Services INSTANCE;

    private EventBus eventBus;

    private CafeRepository cafeRepository;
    private CoffeeDrinkRepository coffeeDrinkRepository;
    private EmployeeRepository employeeRepository;

    private StockService stockService;
    private CafeStatusService cafeStatusService;
    private SalesService salesService;
    private BalanceService balanceService;
    private StaffMessageService staffMessageService;

    private SalesGenerator salesGenerator;
    private StaffMessageGenerator staffMessageGenerator;

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

    public SalesService getSalesService() {
        return salesService;
    }

    public BalanceService getBalanceService() {
        return balanceService;
    }

    public EmployeeRepository getEmployeeRepository() {
        return employeeRepository;
    }
    
    public StaffMessageService getStaffMessageService() {
		return staffMessageService;
	}

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        eventBus = new EventBus();

        cafeRepository = new CafeRepository();
        coffeeDrinkRepository = new CoffeeDrinkRepository();
        employeeRepository = new EmployeeRepository(cafeRepository);

        stockService = new StockService(eventBus);
        // Set up initial stock
        cafeRepository.getCafes().forEach(cafe -> stockService.restock(cafe, new BigDecimal("300")));

        cafeStatusService = new CafeStatusService(eventBus, coffeeDrinkRepository, cafeRepository, stockService);
        salesService = new SalesService(eventBus, stockService);
        balanceService = new BalanceService(eventBus);
        staffMessageService = new StaffMessageService(eventBus);

        salesGenerator = new SalesGenerator(cafeRepository, coffeeDrinkRepository, salesService);
        salesGenerator.start();
        
        staffMessageGenerator = new StaffMessageGenerator(employeeRepository, staffMessageService);
        staffMessageGenerator.start();
        
        INSTANCE = this;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    	staffMessageGenerator.stop();
    	salesGenerator.stop();
    }

    public static Services getInstance() {
        return INSTANCE;
    }
}
