package org.vaadin.samples.cafetycoon.ui.dashboard.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import org.vaadin.samples.cafetycoon.domain.*;

import com.google.common.eventbus.Subscribe;

public class OverviewModel extends AbstractModel {

    public static final String PROP_TOTAL_BALANCE = "totalBalance";
    public static final String PROP_TOTAL_INCOME_24H = "totalIncome24h";
    public static final String PROP_CURRENT_STATUS_AND_INCOME = "currentStatusAndIncome";

    private BigDecimal totalBalance = BigDecimal.ZERO;
    private BigDecimal totalIncome24h = BigDecimal.ZERO;
    private Map<Cafe, CafeStatusAndIncomeDTO> cafes = new HashMap<>();

    @Override
    protected void modelAttached() {
        access(() -> {
            updateBalances();
            updateCafes();
        });
    }

    public synchronized BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public synchronized BigDecimal getTotalIncome24h() {
        return totalIncome24h;
    }

    public synchronized List<CafeStatusAndIncomeDTO> getCurrentStatusAndIncome() {
        List<CafeStatusAndIncomeDTO> list = new ArrayList<>(cafes.values());
        Collections.sort(list, (d1, d2) -> d1.cafe.getName().compareTo(d2.cafe.getName()));
        return list;
    }

    public synchronized Optional<CafeStatusAndIncomeDTO> getCurrentStatusAndIncome(Cafe cafe) {
        return Optional.ofNullable(cafes.get(cafe));
    }

    @Subscribe
    protected synchronized void onCafeStatusChangedEvent(CafeStatusChangedEvent event) {
        access(() -> updateCafe(event.getCafe(), true));
    }

    @Subscribe
    protected synchronized void onSaleEvent(SaleEvent event) {
        access(() -> {
            updateBalances();
            updateCafe(event.getCafe(), true);
        });
    }

    @Subscribe
    protected synchronized void onRestockEvent(RestockEvent event) {
        access(() -> {
            updateBalances();
            updateCafe(event.getCafe(), true);
        });
    }

    private void updateCafes() {
        Services.getInstance().getCafeRepository().getCafes().forEach(cafe -> updateCafe(cafe, false));
        firePropertyChange(PROP_CURRENT_STATUS_AND_INCOME, null, getCurrentStatusAndIncome());
    }

    private void updateCafe(Cafe cafe, boolean fireEvent) {
        CafeStatusAndIncomeDTO old = cafes.get(cafe);
        CafeStatusAndIncomeDTO dto = new CafeStatusAndIncomeDTO(cafe,
            Services.getInstance().getCafeStatusService().getCurrentStatus(cafe),
            Services.getInstance().getSalesService().get24hIncome(cafe));
        cafes.put(cafe, dto);
        if (fireEvent) {
            fireIndexedPropertyChange(PROP_CURRENT_STATUS_AND_INCOME, -1, old, dto);
        }
    }

    private void updateBalances() {
        BigDecimal oldTotalBalance = totalBalance;
        totalBalance = Services.getInstance().getBalanceService().getCurrentTotalBalance();
        firePropertyChange(PROP_TOTAL_BALANCE, oldTotalBalance, totalBalance);

        BigDecimal oldTotalIncome24h = totalIncome24h;
        totalIncome24h = Services.getInstance().getSalesService().getTotal24hIncome();
        firePropertyChange(PROP_TOTAL_INCOME_24H, oldTotalIncome24h, totalIncome24h);
    }

    public static class CafeStatusAndIncomeDTO implements Serializable {
        private final Cafe cafe;
        private final CafeStatus status;
        private final BigDecimal income24h;

        public CafeStatusAndIncomeDTO(Cafe cafe, CafeStatus status, BigDecimal income24h) {
            this.cafe = cafe;
            this.status = status;
            this.income24h = income24h;
        }

        public Cafe getCafe() {
            return cafe;
        }

        public CafeStatus getStatus() {
            return status;
        }

        public BigDecimal getIncome24h() {
            return income24h;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            CafeStatusAndIncomeDTO that = (CafeStatusAndIncomeDTO) o;

            if (!cafe.equals(that.cafe)) {
                return false;
            }
            if (!income24h.equals(that.income24h)) {
                return false;
            }
            if (status != that.status) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = cafe.hashCode();
            result = 31 * result + status.hashCode();
            result = 31 * result + income24h.hashCode();
            return result;
        }
    }
}
