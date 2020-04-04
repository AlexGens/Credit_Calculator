//package com.alexGens.calculation;
//
//import org.joda.time.LocalDate;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class CreditSumAnnuity extends Calculation {
//
//
//    public CreditSumAnnuity(int payoutDuration, double monthPay, double percent, LocalDate startDate) {
//        this.payoutDuration = payoutDuration;
//        this.percent = percent;
//        this.startDate = startDate;
//        this.monthPay = monthPay;
//        setAnnuitKoef();
//        setCreditSum();
//        setPureGraph();
//    }
//
//
//    private void setCreditSum() {
//        double monthPay = this.monthPay;
//        LocalDate date = new LocalDate(startDate.plusMonths(payoutDuration));
//        double creditSum = 0;
//        do {
//            date = date.minusMonths(1);
//            BigDecimal temp =  BigDecimal.valueOf(date.dayOfMonth().getMaximumValue()).
//                    divide(BigDecimal.valueOf(date.dayOfYear().getMaximumValue()), 15, RoundingMode.DOWN).
//                    multiply(BigDecimal.valueOf(this.percent)).add(BigDecimal.valueOf(1));
//            creditSum = BigDecimal.valueOf(monthPay + creditSum).divide(temp,  15, RoundingMode.DOWN).doubleValue();
//        } while  (!date.equals(startDate));
//        this.creditSum = creditSum;
//    }
//}
