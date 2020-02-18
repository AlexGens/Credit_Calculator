package com.example.credit_calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

public class PayoutDurAnnuit extends Calculation {


    public PayoutDurAnnuit(double monthPay, int creditSum, double percent, Calendar startDate) {
        this.creditSum = creditSum;
        this.percent = percent;
        this.startDate = startDate;
        this.monthPay = monthPay;
        setPayoutDuration();
        setAnnuitKoef();
        setPureGraph();
    }



    private void setPayoutDuration() {
        double monthPay = this.monthPay;
        double creditSum = this.creditSum;
        Calendar date = null;
        int payoutDuraion = 0;
        date.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
        while (creditSum != 0) {
            if (creditSum > monthPay) {
                creditSum = creditSum - (monthPay - findMonthPercents((Calendar) date));
                date.add(Calendar.MONTH, 1);
                payoutDuraion++;
            } else {
                creditSum = 0;
                payoutDuraion++;
            }
        }
        this.payoutDuration = payoutDuraion;
    }

    private double findMonthPercents(Calendar date) {
        date.set(Calendar.DAY_OF_MONTH, 1);
        int big = date.getLeastMaximum(Calendar.MONTH);
        int  q = date.getLeastMaximum(Calendar.YEAR);

        return BigDecimal.valueOf(date.getMaximum(Calendar.MONTH)).
                divide(BigDecimal.valueOf(date.getActualMaximum(Calendar.YEAR)), 15, RoundingMode.DOWN).
                multiply(BigDecimal.valueOf(this.percent)).
                multiply(BigDecimal.valueOf(this.creditSum)).
                doubleValue();
    }
}
