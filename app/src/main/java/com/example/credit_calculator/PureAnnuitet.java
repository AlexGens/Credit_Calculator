//package com.example.credit_calculator;
//
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
//
///**
// Думаю над тем как интегрировать частичные погашения в график.
// Идея такая:
//  - В зависимости от набора данных создать по классу для построения "чистого" графика (без частичных погашений)
//  - График частичных погашений построить отдельно в ExtraPaymentsMap
//  - В отдельном классе "совместить" чистый график с графиком погашений.
//
//
// */
//
//public  class PureAnnuitet extends Calculation {
//    private int payoutDuration;
//    private int creditSum;
//    private double percent;
//    private Calendar startDate;
//
//    private double month_pay;
//    private double annuitKoef;
//    private static   TreeMap<Calendar, HashMap<GRAPH_COLUMN, Double>> pureGraph = new TreeMap<>();
//
//
//    public static  TreeMap<Calendar, HashMap<GRAPH_COLUMN, Double>> getGraphMonthPay(int payoutDuration, int creditSum, double percent, Calendar startDate) {
//            pureAnnuitet = new PureAnnuitet(payoutDuration, creditSum, percent, startDate);
//            return pureGraph;
//    }
//    public static TreeMap<Calendar, HashMap<GRAPH_COLUMN, Double>> getGraphPayoutDur(double month_pay, int creditSum, double percent, Calendar startDate) {
//        pureAnnuitet = new PureAnnuitet(month_pay, creditSum, percent, startDate);
//        return pureGraph;
//    }
//    public static TreeMap<Calendar, HashMap<GRAPH_COLUMN, Double>> getGraphCreditSum(int payoutDuration, double month_pay , double percent,  Calendar startDate) {
//        pureAnnuitet = new PureAnnuitet(payoutDuration, month_pay, percent, startDate);
//        return pureGraph;
//    }
//
//
//
//
//    private PureAnnuitet(int payoutDuration, int creditSum, double percent, Calendar startDate) {
//        this.payoutDuration = payoutDuration;
//        this.creditSum = creditSum;
//        this.percent = percent;
//        this.startDate = startDate;
//        setAnnuitKoef();
//        setMonthPay();
//        setPureGraph();
//    }
//
//    private PureAnnuitet(double month_pay, int creditSum, double percent, Calendar startDate) {
//        this.creditSum = creditSum;
//        this.percent = percent;
//        this.startDate = startDate;
//        this.month_pay = month_pay;
//        setPayoutDuration();
//        setAnnuitKoef();
//        setPureGraph();
//    }
//
//    private PureAnnuitet(int payoutDuration, double month_pay , double percent,  Calendar startDate) {
//        this.payoutDuration = payoutDuration;
//        this.percent = percent;
//        this.startDate = startDate;
//        this.month_pay = month_pay;
//        setAnnuitKoef();
//        setCreditSum();
//        setPureGraph();
//    }
//
//    private void setCreditSum() {
//        this.creditSum = BigDecimal.valueOf(this.month_pay).divide(BigDecimal.valueOf(this.annuitKoef), 15, RoundingMode.DOWN).intValue();
//            }
//
//
//    private void setPayoutDuration() {
//        double month_pay = this.month_pay;
//        double creditSum = this.creditSum;
//        Calendar date = null;
//        int payoutDuraion = 0;
//        date.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
//        while (creditSum != 0) {
//            if (creditSum > month_pay) {
//                creditSum = creditSum - (month_pay - findMonthPercents((Calendar) date));
//                date.add(Calendar.MONTH, 1);
//                payoutDuraion++;
//            } else {
//                creditSum = 0;
//                payoutDuraion++;
//            }
//        }
//        this.payoutDuration = payoutDuraion;
//    }
//
//    private void setMonthPay() {
//        this.month_pay = creditSum * annuitKoef;
//    }
//
//    private double findMonthPercents(GregorianCalendar date) {
//        date.set(Calendar.DAY_OF_MONTH, 1);
//        int big = date.getLeastMaximum(Calendar.MONTH);
//        int  q = date.getLeastMaximum(Calendar.YEAR);
//
//        return BigDecimal.valueOf(date.getMaximum(Calendar.MONTH)).
//                divide(BigDecimal.valueOf(date.getActualMaximum(Calendar.YEAR)), 15, RoundingMode.DOWN).
//                multiply(BigDecimal.valueOf(this.percent)).
//                multiply(BigDecimal.valueOf(this.creditSum)).
//                doubleValue();
//    }
//
//    private double findAccurateMonthPercents(Calendar prevPay, Calendar acPay, ExtraPaymentsMap map) {
//        Calendar date = null;
//        date.set(prevPay.get(Calendar.YEAR), prevPay.get(Calendar.MONTH), prevPay.get(Calendar.DAY_OF_MONTH));
//        double monthPercents = 0;
//        for (int i = 0; i < prevPay.getActualMaximum(Calendar.MONTH); i++) {
//            date.add(Calendar.DAY_OF_MONTH, 1);
//            try {
//                double extraPayValue = map.getValue(date);
//                if (Math.max(extraPayValue, monthPercents) == extraPayValue || extraPayValue == monthPercents) {
//                    extraPayValue = extraPayValue - monthPercents;
//                    monthPercents = 0;
//                    this.creditSum = this.creditSum - (int) extraPayValue;
//                } else {
//                    monthPercents = monthPercents - extraPayValue;
//                }
//
//            } catch (NullPointerException e) { // Нет частичных погашений в этот день }
//                monthPercents = monthPercents
//                        + BigDecimal.valueOf(this.percent).
//                        multiply(BigDecimal.valueOf(this.creditSum)).
//                        divide(BigDecimal.valueOf(prevPay.getActualMaximum(Calendar.YEAR)), 15, RoundingMode.DOWN).
//                        doubleValue();
//            }
//
//        }
//        return monthPercents;
//    }
//}






