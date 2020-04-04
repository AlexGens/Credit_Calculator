package com.alexGens.NewCalculation;

import android.annotation.SuppressLint;

import com.alexGens.calculation.GraphColumn;
import com.alexGens.credit_calculator.AmountOrTerm;
import com.alexGens.credit_calculator.CreditType;
import com.alexGens.credit_calculator.NeedToFind;
import com.alexGens.extraPayments.ExtraPaymentsMap;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.alexGens.calculation.GraphColumn.BASIC_PAYMENT;
import static com.alexGens.calculation.GraphColumn.FULL_CRED_SUM;
import static com.alexGens.calculation.GraphColumn.FULL_PAYMENT;
import static com.alexGens.calculation.GraphColumn.PERCENTS;
import static com.alexGens.credit_calculator.AmountOrTerm.AMOUNT_REDUCE;
import static com.alexGens.credit_calculator.AmountOrTerm.TERM_REDUCE;
import static com.alexGens.credit_calculator.CreditType.ANNUITY;


public class NewCalculation {
    int payoutDuration;
    double percentRate;
    private BigDecimal annuitetKoef;
    final LocalDate startDate;
    LocalDate prevPurePayDay;
    LocalDate purePayDay;
    LocalDate lastPayDay;
    LocalDate nowPayDay;
    double monthPay;
    double basicPay;
    double creditSum;
    int counter = 1;
    String format = "%.2f";
    CreditType creditType;
    TreeMap<Integer, HashMap<GraphColumn, String>> pureGraph = new TreeMap<>();

    public TreeMap<Integer, HashMap<GraphColumn, String>> getPureGraph() {
        return pureGraph;
    }

    ExtraPaymentsMap map;
    private ExtraPaymentsMap extraPaymentsMap;

    public NewCalculation( double creditSum, int payoutDuration, double percentRate, LocalDate startDate, ExtraPaymentsMap extraPaymentsMap) {
        this.payoutDuration = payoutDuration;
        this.startDate = startDate;
        this.creditSum = creditSum;
        this.percentRate = percentRate;
        this.purePayDay = startDate.plusMonths(1);
        this.lastPayDay = startDate;
        this.extraPaymentsMap = extraPaymentsMap;
        this.creditType = ANNUITY;
        setAnnuitetKoef(payoutDuration);
        this.monthPay = setMonthPay(creditSum);
        setPureGraph();

    }

    public NewCalculation(double creditSum, double monthPay,  double percentRate, LocalDate startDate,  ExtraPaymentsMap extraPaymentsMap) {
        this.creditSum = creditSum;
        this.percentRate = percentRate;
        this.startDate = startDate;
        this.monthPay = monthPay;
        this.purePayDay = startDate.plusMonths(1);
        this.lastPayDay = startDate;
        this.extraPaymentsMap = extraPaymentsMap;
        this.creditType = ANNUITY;
        setPayoutDuration();
        setAnnuitetKoef(payoutDuration);
        setPureGraph();
    }

    public NewCalculation(int payoutDuration, double monthPay, double percentRate, LocalDate startDate,  ExtraPaymentsMap extraPaymentsMap) {
        this.payoutDuration = payoutDuration;
        this.percentRate = percentRate;
        this.startDate = startDate;
        this.monthPay = monthPay;
        this.purePayDay = startDate.plusMonths(1);
        this.lastPayDay = startDate;
        this.extraPaymentsMap = extraPaymentsMap;
        this.creditType = ANNUITY;
        setAnnuitetKoef(payoutDuration);
        setCreditSum();
        setPureGraph();
    }

    public NewCalculation(int payoutDuration, double creditSum, double percentRate, LocalDate startDate, CreditType creditType, ExtraPaymentsMap extraPaymentsMap) {
        this.payoutDuration = payoutDuration;
        this.creditSum = creditSum;
        this.percentRate = percentRate;
        this.startDate = startDate;
        this.creditType =  creditType;
        this.purePayDay = startDate.plusMonths(1);
        this.lastPayDay = startDate;
        this.extraPaymentsMap = extraPaymentsMap;
        setBasicPay();
        setPureGraph();
    }

    private void setBasicPay() {
        this.basicPay = creditSum / payoutDuration;
    }

    private void setAnnuitetKoef(int payoutDuration) {
        BigDecimal monthPercent = BigDecimal.valueOf(percentRate).divide(BigDecimal.valueOf(12), 15, RoundingMode.DOWN);
        BigDecimal inBreakets = monthPercent.add(BigDecimal.valueOf(1));
        BigDecimal percentInPow = inBreakets.pow(payoutDuration);
        BigDecimal firstToDivide = percentInPow.multiply(monthPercent);
        BigDecimal secondToDivide = percentInPow.subtract(BigDecimal.valueOf(1));
        this.annuitetKoef = firstToDivide.divide(secondToDivide, 20, RoundingMode.DOWN);
    }

    private void setMonthPay(double creditSum, boolean isAfterExtraPay) {
        double monthPayLocal = this.monthPay;
        setAnnuitetKoef(payoutDuration - 1);
        double basicPayLocal;
        double creditSumLocal;
        double monthPayPrev;
        double percentsToNextPayment = findPercents(lastPayDay, purePayDay, creditSum);
        do  {
            basicPayLocal = monthPayLocal - percentsToNextPayment;
            creditSumLocal = creditSum - basicPayLocal;
            monthPayPrev = monthPayLocal;
            monthPayLocal = setMonthPay(creditSumLocal);
        } while (!monthPayChecked(monthPayPrev , monthPayLocal));
        this.monthPay = monthPayLocal;
    }
    private boolean monthPayChecked(double monthPayPrev, double monthPayLocal) {
        return Math.abs(monthPayLocal - monthPayPrev)  * 100 < 1;
    }

    private double setMonthPay(double creditSum) {
     //   double correction = setCorrection();
        return BigDecimal.valueOf(creditSum).multiply(this.annuitetKoef).doubleValue(); //.multiply(BigDecimal.valueOf(correction)).doubleValue() ;
    }

    private double setCorrection() {
        double truly = 0;
        LocalDate startDateLocal = new LocalDate(startDate);
        LocalDate year = startDateLocal.plusYears(1);
        for (int i = 0; i < payoutDuration; i++) {
            if (year.equals(startDateLocal)) {
                year = year.plusYears(1);
            }
            truly = truly + (double) Days.daysBetween(startDateLocal, startDateLocal.plusMonths(1)).getDays() / (Days.daysBetween(year.minusYears(1), year).getDays());

            startDateLocal = startDateLocal.plusMonths(1);
        }
        truly = (truly / Years.yearsBetween(startDate, startDate.plusMonths(payoutDuration)).getYears()) < 1.0 &&
                truly / Years.yearsBetween(startDate, startDate.plusMonths(payoutDuration)).getYears() >= 0.999999999 ?
                Math.ceil(truly) :
                truly;
        double rounded = (double) payoutDuration / 12;
        return 1;
    }
    private void setPayoutDuration() {
        double monthPay = this.monthPay;
        double creditSum = this.creditSum;
        LocalDate date = new LocalDate(startDate);
        int payoutDuration = 0;

        while (creditSum != 0) {
            if (creditSum > monthPay) {
                creditSum = creditSum - (monthPay - findPercents(date, date.plusMonths(1) , creditSum));
                date = date.plusMonths(1);
                payoutDuration++;
            } else {
                creditSum = 0;
                payoutDuration++;
            }
        }
        this.payoutDuration = payoutDuration;
    }
    private void setPayoutDuration(boolean isDifferentied) {
        this.payoutDuration = (int) Math.round(this.creditSum / this.basicPay);
    }

    private void setCreditSum() {
        double monthPay = this.monthPay;
        LocalDate date = new LocalDate(startDate.plusMonths(payoutDuration));
        double creditSum = 0;
        do {
            BigDecimal daysOfMonthInYear = getDaysOfMonthInYear(date.minusMonths(1), date);
            BigDecimal temp =  daysOfMonthInYear.multiply(BigDecimal.valueOf(this.percentRate)).add(BigDecimal.valueOf(1));
            creditSum = BigDecimal.valueOf(monthPay + creditSum).divide(temp,  15, RoundingMode.DOWN).doubleValue();
        } while  (!date.equals(startDate));
        this.creditSum = creditSum;
    }

    protected void setPureGraph() {
        while (creditSum > 0) {
            if (extraPaymentsMap.size() > 0 &&  extraPaymentsMap.firstKey().compareTo(purePayDay) < 0) {
                addExtraPayment(extraPaymentsMap.pollFirstEntry());
            } else {
                addPurePayment();
            }
        }
    }

    private void addPurePayment() {
        nowPayDay = purePayDay;
        double beforePayCreditSum = creditSum;
        double percents = findPercents(lastPayDay, nowPayDay, beforePayCreditSum);
        double monthPay;
        double basicPay;
        double afterPayCreditSum;
        if (creditType == ANNUITY) {
            monthPay = this.monthPay;
            basicPay = monthPay - percents;
        } else {
            basicPay = this.basicPay;
            monthPay = basicPay + percents;
        }
        if (beforePayCreditSum > basicPay) {
            afterPayCreditSum = beforePayCreditSum - basicPay;
        } else {
            afterPayCreditSum = 0;
            basicPay = beforePayCreditSum;
            monthPay = basicPay + percents;
        }
        addToMap(afterPayCreditSum, monthPay, percents, basicPay);
        setNewData(afterPayCreditSum, nowPayDay, true);
    }


    //--------------------------------------------------------------------------------------------------

    /**
     * Метод расчитывает проценты, при внесении частичного погашения,
     * после чего вносит данные в график платежей,
     * и перерасчитывает оставшуюся сумму долга, платёж или срок кредита.
     */
    private void addExtraPayment(Map.Entry<LocalDate, HashMap<AmountOrTerm, Double>> entry) {
        double monthPay;
        double percents;
        double basicPay;
        double beforePayCreditSum;
        double afterPayCreditSum;

        AmountOrTerm aot;
        nowPayDay = entry.getKey();
        beforePayCreditSum = creditSum;
        aot = entry.getValue().containsKey(AMOUNT_REDUCE) ? AMOUNT_REDUCE : TERM_REDUCE;
        monthPay = entry.getValue().get(aot);
        percents = findPercents(lastPayDay, nowPayDay, beforePayCreditSum);
        basicPay = monthPay < percents ? 0 : monthPay - percents;
        if (beforePayCreditSum > basicPay) {
            afterPayCreditSum = beforePayCreditSum - monthPay + percents;
        } else {
            basicPay = beforePayCreditSum;
            monthPay = basicPay + percents;
            afterPayCreditSum = 0;
        }


        addToMap(afterPayCreditSum, monthPay, percents, basicPay);
        setNewData(afterPayCreditSum, nowPayDay);
        if (aot == AMOUNT_REDUCE) {
            if (creditType == ANNUITY) {
                setMonthPay(afterPayCreditSum, true);
            } else {setBasicPay();}
        } else {
            if (creditType == ANNUITY) {
                setPayoutDuration();
            } else {setPayoutDuration(true);}
        }

    }



    //---------------------------------------------------------------------------------------------------------

    /**
     * Метод обновляет:
     * - сумму оставшегося долга по кредиту
     * - дату последнего платежа
     * - счётчик количества платежей
     * - дату следующего планового платежа (только если после планового платежа)
     * - общий срок кредита (только если после планового платежа)
     * @param afterPayCreditSum - Сумма оставшегося долга по кредиту
     * @param nowPayDay - Дата совершённого платежа
     */
    private void setNewData(double afterPayCreditSum, LocalDate nowPayDay) {
        creditSum = afterPayCreditSum;
        this.lastPayDay = nowPayDay;
        counter++;
    }
    private void setNewData(double afterPayCreditSum, LocalDate nowPayDay, boolean isPurePayDay) {
        setNewData(afterPayCreditSum, nowPayDay);
        purePayDay = purePayDay.plusMonths(1);
        payoutDuration--;
    }

//---------------------------------------------------------------------------------------------------------
    /**
     * Метод добавляет данные в график платежей.
     * @param payDayCreditSum - Сумма "чистого" оставшегося долга, без учёта процентов
     * @param monthPay - Платёж
     * @param percents - Начисленные проценты за период
     * @param basicPay - Платёж без учёта процентов. Сумма, на которую уменьшился основной долг.
     */

    @SuppressLint("DefaultLocale")
    private void addToMap(double payDayCreditSum, double monthPay, double percents, double basicPay) {
        HashMap<GraphColumn, String> temp = new HashMap<>();
        temp.put(GraphColumn.PERCENTS, String.format(format, percents));
        temp.put(GraphColumn.FULL_PAYMENT,  String.format(format, monthPay));
        temp.put(GraphColumn.BASIC_PAYMENT,  String.format(format, basicPay));
        temp.put(GraphColumn.FULL_CRED_SUM, String.format(format, payDayCreditSum));
        temp.put(GraphColumn.DATE, nowPayDay.toString("dd.MM.yyyy"));
        pureGraph.put(counter, temp);
    }
//---------------------------------------------------------------------------------------------------------

    /**
     * Метод расчитывает начисляемые за период проценты.
     * Учитывается наступление високосного года {@See <a href=http://www.consultant.ru/edu/student/consultation/protsenty_po_kreditu/</a>}
     * @param lastPayDay - Дата прошлого платёжа
     * @param nowPayDay - Дата текущего платежа
     * @param creditSum - Оставшаяся сумма долга по кредиту
     */
    protected double findPercents(LocalDate lastPayDay, LocalDate nowPayDay, double creditSum) {
        BigDecimal daysOfMonthInYear = getDaysOfMonthInYear(lastPayDay, nowPayDay);
        return daysOfMonthInYear.
                multiply(BigDecimal.valueOf(this.percentRate)).
                multiply(BigDecimal.valueOf(creditSum)).
                doubleValue();
    }
    private BigDecimal getDaysOfMonthInYear(LocalDate lastPayDay, LocalDate nowPayDay) {
        BigDecimal daysOfMonthInYear;
        if (!lastPayDay.year().equals(nowPayDay.year())) {
            BigDecimal firstPart = BigDecimal.valueOf(lastPayDay.dayOfYear().getMaximumValue() - lastPayDay.getDayOfYear()).divide(BigDecimal.valueOf(lastPayDay.dayOfYear().getMaximumValue()), 15, RoundingMode.DOWN);
            BigDecimal secondPart = BigDecimal.valueOf(nowPayDay.getDayOfYear()).divide(BigDecimal.valueOf(nowPayDay.dayOfYear().getMaximumValue()), 15, RoundingMode.DOWN);
            daysOfMonthInYear = firstPart.add(secondPart);
        } else {
            daysOfMonthInYear = BigDecimal.valueOf(lastPayDay.dayOfMonth().getMaximumValue()).divide(BigDecimal.valueOf(lastPayDay.dayOfYear().getMaximumValue()), 15, RoundingMode.DOWN);
    }
        return daysOfMonthInYear;
}
}
