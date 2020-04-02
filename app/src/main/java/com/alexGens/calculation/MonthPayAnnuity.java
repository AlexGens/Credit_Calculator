package com.alexGens.calculation;

import org.joda.time.LocalDate;

import java.math.BigDecimal;

public class MonthPayAnnuity extends Calculation {

    public MonthPayAnnuity(int payoutDuration, double creditSum, double percent, LocalDate startDate) {
        super();
        super.payoutDuration = payoutDuration;
        this.creditSum = creditSum;
        this.percent = percent;
        this.startDate = startDate;
        setAnnuitKoef();
        setMonthPay();
        setPureGraph();
    }

    private void setMonthPay() {
        this.monthPay = BigDecimal.valueOf(this.creditSum).multiply(this.annuitKoef).doubleValue();
    }

}
