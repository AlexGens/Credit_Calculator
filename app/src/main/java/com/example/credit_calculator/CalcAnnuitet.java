//package com.example.credit_calculator;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.Calendar;
//import java.util.GregorianCalendar;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.TreeMap;
//
//public class CalcAnnuitet {
//    private int creditSum;
//    private double percent;
//    private double month_pay;
//    private int payoutDuration;
//    private double dayPercent;
//    private enum GRAPH_COLUMN {PERCENTS, PURE_PAYMENT, FULL_PAYMENT}
//    private ExtraPaymentsMap extraPaymentsMap;
//    private TreeMap<GregorianCalendar, HashMap<GRAPH_COLUMN, Double>> graf;
//    private TreeMap<GregorianCalendar, Double> percents;
//    private TreeMap<GregorianCalendar, Integer> pure_payment;
//    private TreeMap<GregorianCalendar, Double> fullDept;
//    private GregorianCalendar startDay;
//    private GregorianCalendar lastPay;
//    private static CalcAnnuitet instance;
//    private AmountOrTerm aot;
//
//    public static CalcAnnuitet getInstanceMonthPay(int creditSum, double percent, int payoutDuration, int year, int month, int day, ExtraPaymentsMap extraPaymentsMap) {
//        if (instance == null) {
//            instance = new CalcAnnuitet(creditSum, percent, payoutDuration, year, month, day, extraPaymentsMap);
//        }
//        return instance;
//    }
//    public static CalcAnnuitet getInstancePayoutDur(ExtraPaymentsMap extraPaymentsMap, double percent, int month_pay, int year, int month, int day,  int creditSum) {
//        if (instance == null) {
//            instance = new CalcAnnuitet( extraPaymentsMap,  percent,  month_pay,  year,  month,  day,   creditSum);
//        }
//        return instance;
//    }
//    public static CalcAnnuitet getInstanceCredSum(int month_pay, double percent,ExtraPaymentsMap extraPaymentsMap , int year, int month, int day, int payoutDuration) {
//        if (instance == null) {
//            instance = new CalcAnnuitet( month_pay,  percent, extraPaymentsMap ,  year,  month,  day,  payoutDuration);
//        }
//        return instance;
//    }
//
//    private CalcAnnuitet(int creditSum, double percent, int payoutDuration, int year, int month, int day, ExtraPaymentsMap extraPaymentsMap) {
//        this.creditSum = creditSum;
//        this.percent = percent;
//        this.payoutDuration = payoutDuration;
//        this.extraPaymentsMap = extraPaymentsMap;
//        startDay.set(year, month, day);
//        lastPay.set(year, month, day);
//        setLastPay();
//        setMonthPay(this.payoutDuration);
//        setDayPercent();
//    }
//
//    private CalcAnnuitet(ExtraPaymentsMap extraPaymentsMap, double percent, int month_pay, int year, int month, int day,  int creditSum) {
//    }
//    private CalcAnnuitet(int month_pay, double percent,ExtraPaymentsMap extraPaymentsMap , int year, int month, int day, int payoutDuration) {
//    }
//
//    private void setMonthPay(int payoutDuration) {
//       double koef = findCoef(payoutDuration).doubleValue();
//       month_pay = (double) Math.round((this.creditSum * koef));
//   }
//   private void setPayoutDuration(double month_pay,int prevCreditSum, Calendar prPay, Calendar exPay ) {
//        int creditSum = this.creditSum;
//        while (creditSum > 0) {
//        month_pay - monthPercents()
//        }
//   }
//    private void setLastPay() {
//        lastPay.add(Calendar.MONTH, payoutDuration);
//    }
//
//    private void  calculation() {
//        Calendar prevPay = GregorianCalendar.getInstance();
//        Calendar actualPay = GregorianCalendar.getInstance();
//        prevPay.set(startDay.get(Calendar.YEAR), startDay.get(Calendar.MONTH), startDay.get(Calendar.DAY_OF_MONTH));
//        actualPay.set(startDay.get(Calendar.YEAR), startDay.get(Calendar.MONTH), startDay.get(Calendar.DAY_OF_MONTH));
//        for (int i = 1; i <= payoutDuration; i++) {
//            findExtraPayInPeriod(prevPay, actualPay);
//        actualPay.add(Calendar.MONTH, 1);
//        double monthPercents = monthPercents(prevPay.getActualMaximum(Calendar.DAY_OF_MONTH), this.creditSum);
//        double purePay = month_pay - monthPercents;
//
//
//        }
//    }
//
//    private void findExtraPayInPeriod(Calendar prPay, Calendar acPay) {
//        if (extraPaymentsMap == null) {}
//        else {
//            Iterator<Map.Entry<GregorianCalendar, Integer>> iterator = extraPaymentsMap.getAllValues().entrySet().iterator();
//            while (iterator.hasNext()) {
//                Map.Entry<GregorianCalendar, Integer> entry = iterator.next();
//                if (entry.getKey().before(acPay) && entry.getKey().equals(acPay) && entry.getKey().after(prPay)) {
//                    int prevCreditSum = this.creditSum;
//                    this.creditSum = this.creditSum - entry.getValue();
//
//                    if (extraPaymentsMap.getAoT(entry.getKey()) == AmountOrTerm.AMOUNT_REDUCE) {
//                        setMonthPay(this.payoutDuration);
//                    } else if (extraPaymentsMap.getAoT(entry.getKey()) == AmountOrTerm.TERM_REDUCE) {
//                        setPayoutDuration(this.month_pay, prevCreditSum, prPay, entry.getKey());
//                    }
//                    percents.put(entry.getKey(), 0.0);
//                    pure_payment.put(entry.getKey(), entry.getValue());
//                    fullDept.put(entry.getKey(), (double) entry.getValue());
//                }
//            }
//        }
//    }
//
//    private double monthPercents(int daysInMonth, int creditSum) {
//        return BigDecimal.valueOf(creditSum).multiply(BigDecimal.valueOf(dayPercent))
//                .divide(BigDecimal.valueOf(100), 5, RoundingMode.DOWN).multiply(BigDecimal.valueOf(daysInMonth)).doubleValue();
//   }
//    private double monthPercents(int daysInMonth, int prevCreditSum, Calendar prPay,Calendar exPay) {
//
//        return BigDecimal.valueOf(creditSum).multiply(BigDecimal.valueOf(dayPercent))
//                .divide(BigDecimal.valueOf(100), 5, RoundingMode.DOWN);
//    }
//   private double setDayPercent() {
//        BigDecimal temp = BigDecimal.valueOf(this.percent).divide(BigDecimal.valueOf(365), 5, RoundingMode.DOWN);
//        return temp.doubleValue();
//   }
//   private BigDecimal findCoef(int payoutDuration) {
//        BigDecimal monthPercent = BigDecimal.valueOf(this.percent).divide(BigDecimal.valueOf(12), 5, RoundingMode.DOWN);
//        BigDecimal inBreakets = monthPercent.add(BigDecimal.valueOf(1));
//        BigDecimal percentInPow = inBreakets.pow(payoutDuration);
//        BigDecimal firstToDivide = percentInPow.multiply(monthPercent);
//        BigDecimal secondToDivide = percentInPow.subtract(BigDecimal.valueOf(1));
//        BigDecimal divided = firstToDivide.divide(secondToDivide, 5, RoundingMode.DOWN );
//        return divided;
//    }
//
//
//}
