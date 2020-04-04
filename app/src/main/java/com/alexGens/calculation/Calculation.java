//package com.alexGens.calculation;
//
//import org.joda.time.Days;
//import org.joda.time.LocalDate;
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.HashMap;
//import java.util.TreeMap;
//
// public abstract class Calculation {
//
//     /**
//      * Calculation - базовый абстрактный класс для расчёта кредита, содержащий основные методы для этого.
//      * setAnnuitKoef() - расчитывает коэффициент аннуитета.
//      * findMonthPercents() - находит сумму насчитанных банком процентов за очередной месяц.
//      * setPureGraph() - заносит в TreeMap pureGraph все необходимые данные для постровения графика платежей.
//      * Расчёт производится классами наследниками, в соответствии с предоставленными данными.
//      * Классы пакета calculation НЕ УЧИТЫВАЮТ частичные погашения.
//      * Для корректировки графика с учётом частичных погашний используются классы пакета extraPayments.
//
//      */
//     int payoutDuration;
//     double percent;
//     BigDecimal annuitKoef;
//     LocalDate startDate;
//     double monthPay;
//     double creditSum;
//     LocalDate year;
//     TreeMap<LocalDate, HashMap<GraphColumn, Double>> pureGraph = new TreeMap<>();
//
//
//     void setAnnuitetKoef() {
//      BigDecimal monthPercent = BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(12), 15, RoundingMode.DOWN);
//      BigDecimal inBreakets = monthPercent.add(BigDecimal.valueOf(1));
//      BigDecimal percentInPow = inBreakets.pow(payoutDuration);
//      BigDecimal firstToDivide = percentInPow.multiply(monthPercent);
//      BigDecimal secondToDivide = percentInPow.subtract(BigDecimal.valueOf(1));
//      this.annuitKoef = firstToDivide.divide(secondToDivide, 20, RoundingMode.DOWN);
//   }
//  /*   protected void setAnnuitKoef(int x) {
//        double inBreakets = Math.pow(Math.pow(1 + percent, (1.0/12.0)), payoutDuration);
//        double secondBreakets = Math.pow(1 + percent, 1.0 / 12) - 1;
//       //this.annuitKoef = inBreakets / (inBreakets - 1) * secondBreakets;
//     }
//*/
//   protected void setPureGraph() {
//      for (int i = 1; i <= payoutDuration; i++) {
//         HashMap<GraphColumn, Double> temp = new HashMap<>();
//          double monthPercents = findMonthPercents(startDate);
//         if ((creditSum + monthPercents) < monthPay) {
//             monthPay = creditSum + monthPercents;
//         }
//
//         temp.put(GraphColumn.PERCENTS, monthPercents);
//         temp.put(GraphColumn.FULL_PAYMENT, monthPay);
//         temp.put(GraphColumn.BASIC_PAYMENT, monthPay - monthPercents);
//         creditSum = creditSum + monthPercents == monthPay ? 0 : creditSum - (monthPay - monthPercents);
//         temp.put(GraphColumn.FULL_CRED_SUM, creditSum);
//         startDate = startDate.plusMonths(1);
//         pureGraph.put(startDate, temp);
//      }
//   }
//     protected double findMonthPercents(LocalDate date) {
//       BigDecimal daysOfMonthInYear;
//                if (!date.year().equals(date.plusMonths(1).year())) {
//                 BigDecimal firstPart = BigDecimal.valueOf(date.dayOfYear().getMaximumValue() - date.getDayOfYear()).divide(BigDecimal.valueOf(date.dayOfYear().getMaximumValue()), 15, RoundingMode.DOWN);
//                 BigDecimal secondPart = BigDecimal.valueOf(date.plusMonths(1).getDayOfYear()).divide(BigDecimal.valueOf(date.dayOfYear().getMaximumValue()), 15, RoundingMode.DOWN);
//                 daysOfMonthInYear = firstPart.add(secondPart);
//                } else { daysOfMonthInYear = BigDecimal.valueOf(date.dayOfMonth().getMaximumValue()).divide(BigDecimal.valueOf(date.dayOfYear().getMaximumValue()), 15, RoundingMode.DOWN);}
//         return daysOfMonthInYear.
//                 multiply(BigDecimal.valueOf(this.percent)).
//                 multiply(BigDecimal.valueOf(this.creditSum)).
//                 doubleValue();
//
//
//      }
//     protected double findMonthPercents(LocalDate date, double creditSum) {
//         BigDecimal daysOfMonthInYear;
//         if (!date.year().equals(date.plusMonths(1).year())) {
//             BigDecimal firstPart = BigDecimal.valueOf(date.dayOfYear().getMaximumValue() - date.getDayOfYear()).divide(BigDecimal.valueOf(date.dayOfYear().getMaximumValue()), 15, RoundingMode.DOWN);
//             BigDecimal secondPart = BigDecimal.valueOf(date.plusMonths(1).getDayOfYear()).divide(BigDecimal.valueOf(date.dayOfYear().getMaximumValue()), 15, RoundingMode.DOWN);
//             daysOfMonthInYear = firstPart.add(secondPart);
//         } else { daysOfMonthInYear = BigDecimal.valueOf(date.dayOfMonth().getMaximumValue()).divide(BigDecimal.valueOf(date.dayOfYear().getMaximumValue()), 15, RoundingMode.DOWN);}
//         return daysOfMonthInYear.
//                 multiply(BigDecimal.valueOf(this.percent)).
//                 multiply(BigDecimal.valueOf(creditSum)).
//                 doubleValue();
//     }
//
//     public TreeMap<LocalDate, HashMap<GraphColumn, Double>> getPureGraph() {
//         return pureGraph;
//     }
//
// }
