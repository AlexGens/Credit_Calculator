package com.alexGens.extraPayments;


import com.alexGens.credit_calculator.AmountOrTerm;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ExtraPaymentsMap {
    public enum INTERVAL {NONE, ONE_MONTH, THREE_MONTH, SIX_MONTH, YEAR}
    private TreeMap<LocalDate, HashMap<AmountOrTerm, ArrayList<double[]>>> result = new TreeMap<>();

    public ExtraPaymentsMap() {
    }

    /**
     * Класс создаёт TreeMap (result) и помещает в него значения сумм частичных погашений и стратегий перерасчёта кредита, отсортированных по дате.
     * <p>
     * <p>
     * Описание метода put:
     * Если частичное погашение идёт с периодичностью, заносим его в result несколько раз, согласно периоду.
     * В конце удаляем лишние платежи, которые вышли за пределы графика.
     * В случае совпадения дат частичных погашений, но различных стратегий перерасчёта кредита,
     * в HashMap в словаре result заносятся оба значения.
     * В случае совпадения и дат, и стратегий перерасчёта, суммы складываются в одну.
     */

    public void put(LocalDate date, final Double value, ExtraPaymentsMap.INTERVAL inter, final AmountOrTerm aot) {
        final double interval;
        switch (inter) {
            case NONE:
                interval = 0;break;
            case ONE_MONTH:
                interval = 1;break;
            case THREE_MONTH:
                interval = 3;break;
            case SIX_MONTH:
                interval = 6;break;
            case YEAR:
                interval = 12;break;
            default:
                throw new IllegalStateException("Unexpected value: " + inter);
        }

        double[] oneValue = new double[]{interval, value};
        ArrayList<double[]> allValues = new ArrayList<>();
        allValues.add(oneValue);
        HashMap<AmountOrTerm, ArrayList<double[]>> temp = new HashMap<>();
        temp.put(aot, allValues);
        putToResult(aot, date, temp, oneValue);


    }
    private void putToResult(AmountOrTerm aot, LocalDate date, HashMap<AmountOrTerm, ArrayList<double[]>> temp, double[] oneValue) {
        if (result.containsKey(date)) {
            if (result.get(date).containsKey(aot)) {
                result.get(date).get(aot).add(oneValue);
            } else result.get(date).putAll(temp);
        } else result.put(date, temp);
    }
    private void putToResult(AmountOrTerm aot, LocalDate date, HashMap<AmountOrTerm, ArrayList<double[]>> tempValue, double[] oneValue, boolean isPutByInterval) {
        int interval = (int) oneValue[0];
        if (oneValue[0] != 0) {
            if (result.containsKey(date.plusMonths(interval))) {
                result.get(date.plusMonths(interval)).putAll(tempValue);
            } else {
                result.put(date.plusMonths(interval), tempValue);
            }
        }
    }
    public Map.Entry<LocalDate, HashMap<AmountOrTerm, Double>> pollFirstEntry() {
        LocalDate date = result.firstEntry().getKey();
        final double[] oneValue;
        AmountOrTerm aot = result.firstEntry().getValue().containsKey(AmountOrTerm.AMOUNT_REDUCE ) ? AmountOrTerm.AMOUNT_REDUCE : AmountOrTerm.TERM_REDUCE;
        oneValue = result.firstEntry().getValue().get(aot).get(result.firstEntry().getValue().get(aot).size() - 1);
        HashMap<AmountOrTerm, ArrayList<double[]>> tempValue = new HashMap<>();
        tempValue.put(aot, new ArrayList<double[]>(){{add(oneValue);}});

        TreeMap<LocalDate, HashMap<AmountOrTerm, Double>> res = new TreeMap<>();;
        HashMap<AmountOrTerm, Double> resValue = new HashMap<>();

        putToResult(aot, date, tempValue, oneValue,true);
        resValue.put(aot, oneValue[1]);
        res.put(date, resValue);
        removeFromResult(aot);

        return res.pollFirstEntry();
    }

    private void removeFromResult(AmountOrTerm aot) {
        if (result.firstEntry().getValue().size() > 1) {
            if (result.firstEntry().getValue().get(aot).size() > 1) {
                result.firstEntry().getValue().get(aot).remove(result.firstEntry().getValue().get(aot).size() - 1);
            } else {
                result.firstEntry().getValue().remove(aot);
            }
        } else {
            if (result.firstEntry().getValue().get(aot).size() > 1) {
                result.firstEntry().getValue().get(aot).remove(result.firstEntry().getValue().get(aot).size() - 1);
            } else  result.pollFirstEntry();
        }
    }


    public LocalDate firstKey() {
        return result.firstKey();
    }
    public int size() {
        return result.size();
    }
}
