package com.winter.model.event;

import java.math.BigDecimal;

public class BalanceUpdateEvent implements BeanEvent {
    private Long companyId;
    private String type;
    private BigDecimal balanceUpdate;

    public BalanceUpdateEvent() {
        this.type = BeanEvents.BALANCE_UPDATE.getEvent();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public BigDecimal getBalanceUpdate() {
        return balanceUpdate;
    }

    public void setBalanceUpdate(BigDecimal balanceUpdate) {
        this.balanceUpdate = balanceUpdate;
    }
}
