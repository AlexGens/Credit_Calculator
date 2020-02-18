package com.example.credit_calculator;

import java.util.Calendar;

public class MonthPayAnnuit extends Calculation {

    public MonthPayAnnuit(int payoutDuration, int creditSum, double percent, Calendar startDate) {
        this.payoutDuration = payoutDuration;
        this.creditSum = creditSum;
        this.percent = percent;
        this.startDate = startDate;
        setAnnuitKoef();
        setMonthPay();
        setPureGraph();
    }

    private void setMonthPay() {
        this.monthPay = this.creditSum * this.annuitKoef;
    }
}
