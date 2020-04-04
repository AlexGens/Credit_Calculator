//package com.alexGens.calculation;
//
//import org.joda.time.LocalDate;
//
//public class PayoutDurAnnuity extends Calculation {
//
//
//    public PayoutDurAnnuity(double monthPay, double creditSum, double percent, LocalDate startDate) {
//        this.creditSum = creditSum;
//        this.percent = percent;
//        this.startDate = startDate;
//        this.monthPay = monthPay;
//        super.year = new LocalDate(startDate.plusYears(1));
//        setPayoutDuration();
//        setAnnuitKoef();
//        setPureGraph();
//    }
//
//
//
//    private void setPayoutDuration() {
//        double monthPay = this.monthPay;
//        double creditSum = this.creditSum;
//        LocalDate date = new LocalDate(startDate);
//        int payoutDuration = 0;
//
//        while (creditSum != 0) {
//            if (creditSum > monthPay) {
//                creditSum = creditSum - (monthPay - findMonthPercents(date, creditSum));
//                date = date.plusMonths(1);
//                payoutDuration++;
//            } else {
//                creditSum = 0;
//                payoutDuration++;
//            }
//        }
//        this.payoutDuration = payoutDuration;
//    }
//
//
//}
