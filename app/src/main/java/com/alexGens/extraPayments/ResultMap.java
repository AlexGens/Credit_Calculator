package com.alexGens.extraPayments;

import com.alexGens.calculation.Calculation;
import com.alexGens.calculation.CredSumAnnuity;
import com.alexGens.calculation.GraphColumn;
import com.alexGens.calculation.MonthPayAnnuity;
import com.alexGens.calculation.MonthPayDifferentiated;
import com.alexGens.calculation.PayoutDurAnnuity;
import com.alexGens.credit_calculator.AmountOrTerm;
import com.alexGens.credit_calculator.CreditType;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import static com.alexGens.calculation.GraphColumn.FULL_CRED_SUM;
import static com.alexGens.calculation.GraphColumn.FULL_PAYMENT;
import static com.alexGens.calculation.GraphColumn.PERCENTS;
import static com.alexGens.calculation.GraphColumn.PURE_PAYMENT;
import static com.alexGens.credit_calculator.AmountOrTerm.AMOUNT_REDUCE;
import static com.alexGens.credit_calculator.AmountOrTerm.TERM_REDUCE;

public class ResultMap {

    /**
     * Класс объединяет основной расчёт из пакета calculation с данными о частичных погашениях
     * из класса ExtraPaymentsMap в один итоговый, перерасчитанный TreeMap.
     */
    private TreeMap<LocalDate, HashMap<GraphColumn, Double>> pureGraph;
    private TreeMap<LocalDate, HashMap<AmountOrTerm, Double>> extraPaymentsMap;
    public TreeMap<LocalDate, HashMap<GraphColumn, ArrayList<Double>>> getResultGraph() {
        return resultMap;
    }
    private  TreeMap<LocalDate, HashMap<GraphColumn, ArrayList<Double>>> resultMap;
    private LocalDate startDate;
    private double percentRate;
    private CreditType type;

    private  int payoutDuration;
    private  double percents;
    private  double annuitKoef;
    private  double purePay;
    private  double monthPay;
    private  double creditSum;
    private  double diffPurePay;

    // Для ShowResultsFragment
    private double allPercentsResults;
    private  double allMonthPaymentsResults;
    private  double creditSumResults;
    private   ArrayList<Double> monthPayResults = new ArrayList<>();
    private  int payoutDurationResults;
    private  LocalDate startDateResults;
    private LocalDate finalDateResults;


    public ResultMap(Calculation pureGraph, ExtraPaymentsMap extraPaymentsMap, LocalDate startDate, double creditSum, double monthPay, double percentRate, int payoutDuration) {
        this.pureGraph = pureGraph.getPureGraph();
        try {
            this.extraPaymentsMap = extraPaymentsMap.getExtraPaymentsMap();
        } catch (NullPointerException e) { this.extraPaymentsMap = new TreeMap<>();}
        this.creditSum = creditSum == 0 ? setCreditSum() : creditSum;
        this.monthPay = monthPay == 0 ? this.pureGraph.firstEntry().getValue().get(FULL_PAYMENT) : monthPay;
        this.payoutDuration = payoutDuration == 0 ? this.pureGraph.size() : payoutDuration;
        this.startDate = startDate;
        this.percentRate = percentRate;
        this.type = (pureGraph instanceof MonthPayAnnuity || pureGraph instanceof PayoutDurAnnuity || pureGraph instanceof CredSumAnnuity) ? CreditType.ANNUITY : CreditType.DIFFERENTIETED;
        diffPurePay = type == CreditType.DIFFERENTIETED ? this.pureGraph.firstEntry().getValue().get(PURE_PAYMENT) : 0;
        setStartDataResults();
        setResultMap();
       // setFinalDataResults();
    }

    private void setStartDataResults() {
        creditSumResults = creditSum;
        payoutDurationResults = payoutDuration;
        startDateResults = new LocalDate(startDate);
    }

    private double setCreditSum() {
        double creditSum = 0;
        for ( Map.Entry<LocalDate, HashMap<GraphColumn, Double>> entry: pureGraph.entrySet()
        ) {
            creditSum = entry.getValue().get(PURE_PAYMENT);
        }
        return creditSum;
    }

    private void setResultMap() {
        resultMap = new TreeMap<>();
        Iterator<Map.Entry<LocalDate, HashMap<AmountOrTerm, Double>>> extraPaymentsIterator = null;
        Map.Entry<LocalDate, HashMap<AmountOrTerm, Double>> extraPaymentsEntry = null;
        if (!(extraPaymentsMap == null) && extraPaymentsMap.size() > 0){
            extraPaymentsIterator = extraPaymentsMap.entrySet().iterator();
            extraPaymentsEntry  = extraPaymentsIterator.next();
        }
        Iterator<Map.Entry<LocalDate, HashMap<GraphColumn, Double>>> pureGraphIterator;
        Map.Entry<LocalDate, HashMap<GraphColumn, Double>> pureGraphEntry;
        while (pureGraph.size() > 0) {
             pureGraphIterator = pureGraph.entrySet().iterator();
             pureGraphEntry = pureGraphIterator.next();

             try {
                 while (extraPaymentsMap.size() > 0 && extraPaymentsEntry.getKey().compareTo(pureGraphEntry.getKey()) < 0) {
                     if (extraPaymentsEntry.getValue().size() > 1) {
                         addExtraPaymentToResultMap(extraPaymentsEntry, AMOUNT_REDUCE);
                         extraPaymentsEntry.getValue().remove(AMOUNT_REDUCE);
                         addExtraPaymentToResultMap(extraPaymentsEntry, TERM_REDUCE);

                     } else if (extraPaymentsEntry.getValue().containsKey(AMOUNT_REDUCE)){
                         addExtraPaymentToResultMap(extraPaymentsEntry, AMOUNT_REDUCE);
                     } else if (extraPaymentsEntry.getValue().containsKey(TERM_REDUCE)) {
                         addExtraPaymentToResultMap(extraPaymentsEntry, TERM_REDUCE);
                     }
                     extraPaymentsIterator.remove();
                     pureGraphIterator = pureGraph.entrySet().iterator();
                     if (pureGraphIterator.hasNext()) {
                         pureGraphEntry = pureGraphIterator.next();
                     } else {break;}
                     if (extraPaymentsIterator.hasNext()) {
                         extraPaymentsEntry = extraPaymentsIterator.next();
                     } else {break;}
                 }
             } catch (NullPointerException e){e.printStackTrace();}
             if (pureGraph.size() == 0){break;}
              while (extraPaymentsMap.size() == 0 || extraPaymentsEntry.getKey().compareTo(pureGraphEntry.getKey()) >= 0) {
                 addPureEntryToResultMap(pureGraphEntry);
                 pureGraphIterator.remove();
                 payoutDuration = pureGraph.size();
                 startDate = startDate.plusMonths(1);
                if (pureGraphIterator.hasNext()) {pureGraphEntry = pureGraphIterator.next();}
                else break;
              }

    }

    }
    private void addExtraPaymentToResultMap(Map.Entry<LocalDate, HashMap<AmountOrTerm, Double>> extraPaymentsEntry, AmountOrTerm aot) {
        double lastCredSum;
        double extraPayPercents;
        double extraPayment;
        if (resultMap.size() > 0) {

            Map.Entry<LocalDate, HashMap<GraphColumn, ArrayList<Double>>> resultMapEntry = resultMap.lastEntry();

            lastCredSum = resultMapEntry.getValue().get(FULL_CRED_SUM).get(resultMapEntry.getValue().get(FULL_CRED_SUM).size() - 1);
            extraPayPercents = findDaysPercents(resultMapEntry.getKey(), extraPaymentsEntry.getKey(), lastCredSum); // проценты, начисленные ДО частичного погашения
            extraPayment =  extraPaymentsEntry.getValue().get(aot);                         // сумма частичного погашения
            percents = Math.min(extraPayment, extraPayPercents);                            // Сумма уплачнных процентов, которая пойдет в график. Если сумма платежа меньше чем начисленные проценты
                                                                                            // с предыдущего до данного, то percents = extraPayment, так как мы платежом не смогли покрыть даже начисленные проценты.
                                                                                            // Если же платёж больше, то сумма уплаченных процентов равна начисленным.
            purePay =  extraPayment> extraPayPercents ?  extraPayment - extraPayPercents : 0;       // Сумма, на которую уменьшается основной долг. Если платёж меньше начисленных процентов,
                                                                                                    // он уходит на уплату процентов и основной долг не уменьшается (purePay = 0). Иначе эта сумма равна extraPayment минус начисленные проценты.
            if (creditSum > purePay) {
                creditSum = creditSum - purePay;
            } else {
                extraPayment = creditSum + extraPayPercents;
                purePay = creditSum;
                creditSum = 0;
            }

            if (resultMapEntry.getKey().equals(extraPaymentsEntry.getKey())) {          // Если помимо частичного погашения в этот день был совершён ещё один платёж
                resultMap.get(extraPaymentsEntry.getKey()).get(FULL_CRED_SUM).add(creditSum);
                resultMap.get(extraPaymentsEntry.getKey()).get(PURE_PAYMENT).add(purePay);
                resultMap.get(extraPaymentsEntry.getKey()).get(PERCENTS).add(percents);
                resultMap.get(extraPaymentsEntry.getKey()).get(FULL_PAYMENT).add(extraPayment);
            } else {
                HashMap<GraphColumn, ArrayList<Double>> temp = new HashMap<>();
                temp.put(FULL_CRED_SUM, new ArrayList<Double>());
                temp.get(FULL_CRED_SUM).add(creditSum);
                temp.put(PURE_PAYMENT, new ArrayList<Double>());
                temp.get(PURE_PAYMENT).add(purePay);
                temp.put(PERCENTS, new ArrayList<Double>());
                temp.get(PERCENTS).add(percents);
                temp.put(FULL_PAYMENT, new ArrayList<Double>());
                temp.get(FULL_PAYMENT).add(extraPayment);
                resultMap.put(extraPaymentsEntry.getKey(), temp);
            }

            if (creditSum > 0) {
                double percentsToNextPayment = findDaysPercents(extraPaymentsEntry.getKey(), pureGraph.firstKey(), creditSum);
                if (aot == AMOUNT_REDUCE) {
                    reCalculateWithAmountReduce(percentsToNextPayment);
                } else {
                    reCalculateWithTermReduce(percentsToNextPayment);
                }
            } else {pureGraph = new TreeMap<>();}
        }
        else {
            lastCredSum = creditSum;
            extraPayPercents = findDaysPercents(startDate, extraPaymentsEntry.getKey(), lastCredSum);
            extraPayment = extraPaymentsEntry.getValue().get(aot);
            percents = Math.min(extraPayment, extraPayPercents);
            purePay = extraPayment > extraPayPercents ? extraPayment - extraPayPercents : 0;
            if (creditSum - purePay > 0) {
                creditSum = creditSum - purePay;
            } else {
                extraPayment = creditSum + extraPayPercents;
                purePay = creditSum;
                creditSum = 0;
            }
                HashMap<GraphColumn, ArrayList<Double>> temp = new HashMap<>();
                temp.put(FULL_CRED_SUM, new ArrayList<Double>());
                temp.get(FULL_CRED_SUM).add(creditSum);
                temp.put(PURE_PAYMENT, new ArrayList<Double>());
                temp.get(PURE_PAYMENT).add(purePay);
                temp.put(PERCENTS, new ArrayList<Double>());
                temp.get(PERCENTS).add(percents);  //
                temp.put(FULL_PAYMENT, new ArrayList<Double>());
                temp.get(FULL_PAYMENT).add(extraPayment);
                resultMap.put(extraPaymentsEntry.getKey(), temp);
                if (creditSum > 0) {
                    double percentsToNextPayment = findDaysPercents(extraPaymentsEntry.getKey(), pureGraph.firstKey(), creditSum);
                    if (aot == AMOUNT_REDUCE) {
                        reCalculateWithAmountReduce(percentsToNextPayment);
                    } else {
                        reCalculateWithTermReduce(percentsToNextPayment);
                    }
                } else {pureGraph = new TreeMap<>();}

        }
    }

    private void reCalculateWithTermReduce(double percentsToNextPayment) {
        double credSumLocal;
        pureGraph = new TreeMap<>();
        if (type == CreditType.ANNUITY) {
            if (creditSum > (monthPay - percentsToNextPayment)) {
                credSumLocal = creditSum - (monthPay - percentsToNextPayment);
            } else {
                monthPay = creditSum;
                credSumLocal = 0;
            }
            HashMap<GraphColumn, Double> temp = new HashMap<>();
            temp.put(FULL_CRED_SUM, credSumLocal);
            temp.put(PURE_PAYMENT, (monthPay - percentsToNextPayment));
            temp.put(PERCENTS, percentsToNextPayment);
            temp.put(FULL_PAYMENT, monthPay);
            pureGraph.put(startDate.plusMonths(1), temp);
            if (credSumLocal != 0) {
                PayoutDurAnnuity annuit = new PayoutDurAnnuity(monthPay, credSumLocal, percentRate, startDate.plusMonths(1));
                pureGraph.putAll(annuit.getPureGraph());
            }
        } else {
           if (creditSum > diffPurePay) {
               credSumLocal = creditSum - diffPurePay;
           } else {
               diffPurePay = creditSum;
               credSumLocal = 0;
           }
            HashMap<GraphColumn, Double> temp = new HashMap<>();
            temp.put(FULL_CRED_SUM, credSumLocal);
            temp.put(PURE_PAYMENT, diffPurePay);
            temp.put(PERCENTS, percentsToNextPayment);
            temp.put(FULL_PAYMENT, diffPurePay + percentsToNextPayment);
            pureGraph.put(startDate.plusMonths(1), temp);
        MonthPayDifferentiated differentiated = new MonthPayDifferentiated(diffPurePay, creditSum + percentsToNextPayment, percentRate, startDate.plusMonths(1));
        pureGraph.putAll(differentiated.getPureGraph());

        }
    }




    private void reCalculateWithAmountReduce(double percentsToNextPayment) {
        double credSumLocal;
        if (type == CreditType.ANNUITY) {
            credSumLocal = setNewMonthPayAndCredSum(percentsToNextPayment);
            MonthPayAnnuity annuit = new MonthPayAnnuity(payoutDuration - 1, credSumLocal, percentRate, startDate);
            pureGraph.putAll(annuit.getPureGraph());
        } else {
            diffPurePay = creditSum / payoutDuration;
            credSumLocal = creditSum - diffPurePay;
            HashMap<GraphColumn, Double> temp = new HashMap<>();
            temp.put(FULL_CRED_SUM,credSumLocal);
            temp.put(PURE_PAYMENT, diffPurePay);
            temp.put(PERCENTS, percentsToNextPayment);
            temp.put(FULL_PAYMENT, diffPurePay + percentsToNextPayment);
            pureGraph = new TreeMap<>();
            startDate = startDate.plusMonths(1);
            pureGraph.put(startDate, temp);
            MonthPayDifferentiated difference = new MonthPayDifferentiated(payoutDuration - 1, credSumLocal, percentRate, startDate);
            pureGraph.putAll(difference.getPureGraph());
        }

    }

    private double setNewMonthPayAndCredSum(double percentsToNextPayment) {
        double monthPayLocal = this.monthPay;
        setAnnuitKoef(payoutDuration - 1); // минус 1, так как первый платёж мы добавляем вручную далее
        double purePay;
        double credSumLocal;
        double monthPayPrev;
        do  {
           purePay = monthPayLocal - percentsToNextPayment;
           credSumLocal = this.creditSum - purePay;
           monthPayPrev = monthPayLocal;
           monthPayLocal = setMonthPay(credSumLocal);
        } while (!monthPayChecked(monthPayPrev , monthPayLocal));
        HashMap<GraphColumn, Double> temp = new HashMap<>();
        temp.put(FULL_CRED_SUM,credSumLocal);
        temp.put(PURE_PAYMENT, purePay);
        temp.put(PERCENTS, percentsToNextPayment);
        temp.put(FULL_PAYMENT, monthPayLocal);
        pureGraph = new TreeMap<>();
        startDate = startDate.plusMonths(1);
        pureGraph.put(startDate, temp);
        return credSumLocal;



    }

    private boolean monthPayChecked(double monthPayPrev, double monthPayLocal) {
        return Math.abs((monthPayLocal - monthPayPrev) / Math.max(monthPayLocal, monthPayPrev)) * 100 < 1;
    }

    private double findDaysPercents(LocalDate prevPay, LocalDate nowPay, double credSum) {
        return BigDecimal.valueOf(Days.daysBetween(prevPay, nowPay).getDays()).
                divide(BigDecimal.valueOf(prevPay.dayOfYear().getMaximumValue()), 15, RoundingMode.DOWN).
                multiply(BigDecimal.valueOf(this.percentRate)).
                multiply(BigDecimal.valueOf(credSum)).
                doubleValue();
    }


    protected void setAnnuitKoef(int payoutDuration) {
        BigDecimal monthPercent = BigDecimal.valueOf(percentRate).divide(BigDecimal.valueOf(12), 15, RoundingMode.DOWN);
        BigDecimal inBreakets = monthPercent.add(BigDecimal.valueOf(1));
        BigDecimal percentInPow = inBreakets.pow(payoutDuration);
        BigDecimal firstToDivide = percentInPow.multiply(monthPercent);
        BigDecimal secondToDivide = percentInPow.subtract(BigDecimal.valueOf(1));
        BigDecimal divided = firstToDivide.divide(secondToDivide, 15, RoundingMode.DOWN);
        this.annuitKoef = divided.doubleValue();
    }
    private double setMonthPay(double creditSum) {
        return creditSum * this.annuitKoef;
    }

    private void addPureEntryToResultMap(Map.Entry<LocalDate, HashMap<GraphColumn, Double>> pureGraphEntry) {
        creditSum = pureGraphEntry.getValue().get(FULL_CRED_SUM);
        purePay = pureGraphEntry.getValue().get(PURE_PAYMENT);
        percents = pureGraphEntry.getValue().get(PERCENTS);
        monthPay = pureGraphEntry.getValue().get(FULL_PAYMENT);
        HashMap<GraphColumn, ArrayList<Double>> temp = new HashMap<>();
        temp.put(FULL_CRED_SUM, new ArrayList<Double>());
        temp.get(FULL_CRED_SUM).add(creditSum);
        temp.put(PURE_PAYMENT, new ArrayList<Double>());
        temp.get(PURE_PAYMENT).add(purePay);
        temp.put(PERCENTS, new ArrayList<Double>());
        temp.get(PERCENTS).add(percents);
        temp.put(FULL_PAYMENT, new ArrayList<Double>());
        temp.get(FULL_PAYMENT).add(monthPay);
        resultMap.put(pureGraphEntry.getKey(), temp);
    }

    private void setFinalDataResults() {
        /*
        Фиксируем данные, полученные после перерасчёта.
        Сравниваем первый платёж (основной)
         */
        finalDateResults = new LocalDate(resultMap.lastEntry().getKey());
        if (resultMap.get(startDateResults.plusMonths(1)).get(FULL_PAYMENT).get(0) == resultMap.get(finalDateResults.minusMonths(1)).get(FULL_PAYMENT).get(0)) {
            monthPayResults.add(resultMap.firstEntry().getValue().get(FULL_PAYMENT).get(0));
        } else {
            monthPayResults.add(resultMap.firstEntry().getValue().get(FULL_PAYMENT).get(0));
        }
    }




}
