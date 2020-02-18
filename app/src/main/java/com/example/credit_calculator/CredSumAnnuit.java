package com.example.credit_calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

public class CredSumAnnuit extends Calculation {


    public CredSumAnnuit(int payoutDuration, double monthPay , double percent,  Calendar startDate) {
        this.payoutDuration = payoutDuration;
        this.percent = percent;
        this.startDate = startDate;
        this.monthPay = monthPay;
        setAnnuitKoef();
        setCreditSum();
        setPureGraph();
    }


    private void setCreditSum() {
        this.creditSum = BigDecimal.valueOf(this.monthPay).divide(BigDecimal.valueOf(this.annuitKoef), 15, RoundingMode.DOWN).intValue();
    }
}
