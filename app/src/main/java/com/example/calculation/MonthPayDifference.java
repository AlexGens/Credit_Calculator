package com.example.calculation;

import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.HashMap;


public class MonthPayDifference extends Calculation {
    private double pureMonthPay;

    public MonthPayDifference(int payoutDuration, int creditSum, double percent, LocalDate startDate) {
        this.payoutDuration = payoutDuration;
        this.creditSum = creditSum;
        this.percent = percent;
        this.startDate = startDate;
        setPureMonthPay();
        setPureGraph();
    }

    private void setPureMonthPay() {
        this.pureMonthPay = BigDecimal.valueOf(creditSum).divide(BigDecimal.valueOf(payoutDuration), 15, RoundingMode.DOWN).doubleValue();
    }

    @Override
    protected void setPureGraph() {
        for (int i = 1; i <= payoutDuration; i++) {
            HashMap<GraphColumn, Double> temp = new HashMap<>();
            double monthPercents = findMonthPercents(startDate);
            monthPay = pureMonthPay + monthPercents;
            temp.put(GraphColumn.PERCENTS, monthPercents);
            temp.put(GraphColumn.FULL_PAYMENT, monthPay);
            temp.put(GraphColumn.PURE_PAYMENT, pureMonthPay);
            temp.put(GraphColumn.FULL_CRED_SUM, creditSum - pureMonthPay);
            startDate.plusMonths(1);
            pureGraph.put(startDate, temp);
        }
    }
}
