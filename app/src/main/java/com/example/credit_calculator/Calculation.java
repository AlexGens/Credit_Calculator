package com.example.credit_calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TreeMap;

 abstract class Calculation {

   protected int payoutDuration;
   protected double percent;
   protected double annuitKoef;
   protected Calendar startDate;
   protected double monthPay;
   protected int creditSum;
   protected TreeMap<Calendar, HashMap<GRAPH_COLUMN, Double>> pureGraph = new TreeMap<>();



   protected void setAnnuitKoef() {
      BigDecimal monthPercent = BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(12), 15, RoundingMode.DOWN);
      BigDecimal inBreakets = monthPercent.add(BigDecimal.valueOf(1));
      BigDecimal percentInPow = inBreakets.pow(payoutDuration);
      BigDecimal firstToDivide = percentInPow.multiply(monthPercent);
      BigDecimal secondToDivide = percentInPow.subtract(BigDecimal.valueOf(1));
      BigDecimal divided = firstToDivide.divide(secondToDivide, 15, RoundingMode.DOWN);
      this.annuitKoef = divided.doubleValue();
   }

   protected void setPureGraph() {
      for (int i = 1; i <= payoutDuration; i++) {
         HashMap<GRAPH_COLUMN, Double> temp = new HashMap<>();
         double monthPercents = findMonthPercents(startDate);
         temp.put(GRAPH_COLUMN.PERCENTS, monthPercents);
         temp.put(GRAPH_COLUMN.FULL_PAYMENT, monthPay);
         temp.put(GRAPH_COLUMN.PURE_PAYMENT, monthPay - monthPercents);
         startDate.add(Calendar.MONTH, 1);
         pureGraph.put(startDate, temp);
      }
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
