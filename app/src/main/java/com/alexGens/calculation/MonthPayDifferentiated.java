package com.alexGens.calculation;

import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;


public class MonthPayDifferentiated extends Calculation {


    private double pureMonthPay;

    public MonthPayDifferentiated(int payoutDuration, double creditSum, double percent, LocalDate startDate) {
        this.payoutDuration = payoutDuration;
        this.creditSum = creditSum;
        this.percent = percent;
        this.startDate = startDate;
        setPureMonthPay();
        setPureGraph();
    }



    public MonthPayDifferentiated(double pureMonthPay, double creditSum, double percent, LocalDate startDate) {
        this.pureMonthPay = pureMonthPay;
        this.creditSum = creditSum;
        this.percent = percent;
        this.startDate = startDate;
        setPayoutDuration();
        setPureGraph();
    }
    public double getPureMonthPay() {
        return pureMonthPay;
    }

    private void setPayoutDuration() {
        this.payoutDuration =(int) Math.ceil(creditSum / pureMonthPay);
    }

    private void setPureMonthPay() {
        this.pureMonthPay = BigDecimal.valueOf(creditSum).divide(BigDecimal.valueOf(payoutDuration), 15, RoundingMode.DOWN).doubleValue();
    }

    @Override
    protected void setPureGraph() {
        for (int i = 1; i <= payoutDuration; i++) {
            HashMap<GraphColumn, Double> temp = new HashMap<>();
            double monthPercents = findMonthPercents(startDate);
            if (creditSum < pureMonthPay) {
                pureMonthPay = creditSum;
            }
            monthPay = pureMonthPay + monthPercents;
            temp.put(GraphColumn.PERCENTS, monthPercents);
            temp.put(GraphColumn.FULL_PAYMENT, monthPay);
            temp.put(GraphColumn.PURE_PAYMENT, pureMonthPay);
            temp.put(GraphColumn.FULL_CRED_SUM, creditSum = creditSum - pureMonthPay);
            startDate = startDate.plusMonths(1);
            pureGraph.put(startDate, temp);
        }
    }

}
