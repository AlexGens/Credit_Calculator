package com.example.calculation;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.TreeMap;

 abstract class Calculation {

     protected    int payoutDuration;
     protected   double percent;
     protected   double annuitKoef;
     protected  LocalDate startDate;
     protected  double monthPay;
     protected   double creditSum;
     protected Period plusMonth = Period.months(1);
     protected   TreeMap<LocalDate, HashMap<GraphColumn, Double>> pureGraph = new TreeMap<>();



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
         HashMap<GraphColumn, Double> temp = new HashMap<>();
         if (creditSum < monthPay) {
             monthPay = creditSum;
         }
         double monthPercents = findMonthPercents(startDate);
         temp.put(GraphColumn.PERCENTS, monthPercents);
         temp.put(GraphColumn.FULL_PAYMENT, monthPay);
         temp.put(GraphColumn.PURE_PAYMENT, monthPay - monthPercents);
         temp.put(GraphColumn.FULL_CRED_SUM, creditSum = creditSum - (monthPay - monthPercents));
         startDate.plusMonths(1);
         pureGraph.put(startDate, temp);
      }
   }
     protected double findMonthPercents(LocalDate date) {
         return BigDecimal.valueOf(date.dayOfMonth().getMaximumValue()).
                 divide(BigDecimal.valueOf(date.dayOfYear().getMaximumValue()), 15, RoundingMode.DOWN).
                 multiply(BigDecimal.valueOf(this.percent)).
                 multiply(BigDecimal.valueOf(this.creditSum)).
                 doubleValue();
      }

     public TreeMap<LocalDate, HashMap<GraphColumn, Double>> getPureGraph() {
         return pureGraph;
     }
 }
