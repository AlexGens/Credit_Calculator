package com.example.credit_calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalcAnnuitet {
    private int creditSum;
    private double percent;
    private int payoutDuration;
    public CalcAnnuitet(int creditSum, double percent, int payoutDuration) {
        this.creditSum = creditSum;
        this.percent = percent;
        this.payoutDuration = payoutDuration;

    }

   public int getAnnuitetPay() {
       double koef = findCoef().doubleValue();
       return  (int)Math.round((this.creditSum * koef));
   }
   private BigDecimal getDayPercent() {
        return BigDecimal.valueOf(this.percent).divide(BigDecimal.valueOf(365), 5, RoundingMode.DOWN);
   }
   private BigDecimal findCoef() {
        BigDecimal monthPercent = BigDecimal.valueOf(this.percent).divide(BigDecimal.valueOf(12), 5, RoundingMode.DOWN);
        BigDecimal inBreakets = monthPercent.add(BigDecimal.valueOf(1));
        BigDecimal percentInPow = inBreakets.pow(this.payoutDuration);
        BigDecimal firstToDivide = percentInPow.multiply(monthPercent);
        BigDecimal secondToDivide = percentInPow.subtract(BigDecimal.valueOf(1));
        BigDecimal divided = firstToDivide.divide(secondToDivide, 5, RoundingMode.DOWN );
        return divided;
    }


}
