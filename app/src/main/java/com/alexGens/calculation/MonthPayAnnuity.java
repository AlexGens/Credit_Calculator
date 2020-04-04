//package com.alexGens.calculation;
//
//import org.joda.time.Days;
//import org.joda.time.Interval;
//import org.joda.time.LocalDate;
//import org.joda.time.Period;
//import org.joda.time.Years;
//
//import java.math.BigDecimal;
//
//public class MonthPayAnnuity extends Calculation {
//
//    public MonthPayAnnuity(int payoutDuration, double creditSum, double percent, LocalDate startDate) {
//        super();
//        super.payoutDuration = payoutDuration;
//        this.creditSum = creditSum;
//        this.percent = percent;
//        this.startDate = startDate;
//        super.year = new LocalDate(startDate.plusYears(1));
//        setAnnuitKoef();
//        setMonthPay();
//        setPureGraph();
//        if (pureGraph.lastEntry().getValue().get(GraphColumn.FULL_CRED_SUM) != 0) {
//            reCalculateAnnuity(creditSum, percent, startDate);
//        }
//    }
//
//    private void reCalculateAnnuity(double creditSum, double percent, LocalDate startDate) {
//        double lastCreditSum = pureGraph.lastEntry().getValue().get(GraphColumn.FULL_CRED_SUM);
//        double tempPercent = Math.pow(1 + percent, (double) payoutDuration/12);
//        monthPay = monthPay + (lastCreditSum / tempPercent / payoutDuration);
//        PayoutDurAnnuity annuity = new PayoutDurAnnuity(monthPay, creditSum, percent, startDate);
//        pureGraph = annuity.pureGraph;
//    }
//
//    private void setMonthPay() {
//        double correction = setCorrection();
//        this.monthPay = (BigDecimal.valueOf(this.creditSum).multiply(this.annuitKoef).multiply(BigDecimal.valueOf(correction)).doubleValue());
//    }
//    private double setCorrection() {
//        double truly = 0;
//       LocalDate startDateLocal = new LocalDate(startDate);
//       LocalDate year = startDateLocal.plusYears(1);
//        for (int i = 0; i < payoutDuration ; i++) {
//                if (year.equals(startDateLocal)) {
//                    year = year.plusYears(1);
//                }
//                truly = truly + (double) Days.daysBetween(startDateLocal, startDateLocal.plusMonths(1)).getDays() / (Days.daysBetween(year.minusYears(1), year).getDays());
//
//            startDateLocal = startDateLocal.plusMonths(1);
//        }
//        truly = (truly/ Years.yearsBetween(startDate, startDate.plusMonths(payoutDuration)).getYears()) < 1.0 &&
//                truly/ Years.yearsBetween(startDate, startDate.plusMonths(payoutDuration)).getYears() >= 0.999999999 ?
//                Math.ceil(truly) :
//                truly;
//        double rounded =(double) payoutDuration / 12;
//        return truly/rounded;
//    }
//
//}
