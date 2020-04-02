package com.alexGens.calculation;

import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

public class CredSumAnnuity extends Calculation {


    public CredSumAnnuity(int payoutDuration, double monthPay, double percent, LocalDate startDate) {
        this.payoutDuration = payoutDuration;
        this.percent = percent;
        this.startDate = startDate;
        this.monthPay = monthPay;
        setAnnuitKoef();
        setCreditSum();
        setPureGraph();
    }


    private void setCreditSum(int x) {
        this.creditSum = BigDecimal.valueOf(this.monthPay).divide(this.annuitKoef, 15, RoundingMode.DOWN).intValue();
    }
    private void setCreditSum() {

        double monthPay = this.monthPay;
        LocalDate date = new LocalDate(startDate.plusMonths(payoutDuration));
        double creditSum = 0;
        ArrayList<String> tempo = new ArrayList<>();
        do {
            date = date.minusMonths(1);
            BigDecimal temp =  BigDecimal.valueOf(date.dayOfMonth().getMaximumValue()).
                    divide(BigDecimal.valueOf(date.dayOfYear().getMaximumValue()), 15, RoundingMode.DOWN).
                    multiply(BigDecimal.valueOf(this.percent)).add(BigDecimal.valueOf(1));
            creditSum = BigDecimal.valueOf(monthPay + creditSum).divide(temp,  15, RoundingMode.DOWN).doubleValue();
           tempo.add(String.valueOf((creditSum * temp.subtract(BigDecimal.valueOf(1)).doubleValue())));
        } while  (!date.equals(startDate));
        System.out.println(tempo);
        this.creditSum = creditSum;
    }
}
