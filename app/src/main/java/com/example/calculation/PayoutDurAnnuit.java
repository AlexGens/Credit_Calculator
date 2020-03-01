package com.example.calculation;

import org.joda.time.LocalDate;

import java.util.Calendar;

public class PayoutDurAnnuit extends Calculation {


    public PayoutDurAnnuit(double monthPay, int creditSum, double percent, LocalDate startDate) {
        this.creditSum = creditSum;
        this.percent = percent;
        this.startDate = startDate;
        this.monthPay = monthPay;
        setPayoutDuration();
        setAnnuitKoef();
        setPureGraph();
    }



    private void setPayoutDuration() {
        double monthPay = this.monthPay;
        double creditSum = this.creditSum;
        LocalDate date = LocalDate.fromDateFields(startDate.toDate());
        int payoutDuraion = 0;

        while (creditSum != 0) {
            if (creditSum > monthPay) {
                creditSum = creditSum - (monthPay - findMonthPercents(date));
                date.plusMonths(1);
                payoutDuraion++;
            } else {
                creditSum = 0;
                payoutDuraion++;
            }
        }
        this.payoutDuration = payoutDuraion;
    }


}
