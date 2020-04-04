package com.alexGens.extraPayments;


import com.alexGens.credit_calculator.AmountOrTerm;
import org.joda.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ExtraPaymentsMap {
    public enum INTERVAL {NONE, ONE_MONTH, THREE_MONTH, SIX_MONTH, YEAR}
    private TreeMap<LocalDate, HashMap<AmountOrTerm, double[]>> result = new TreeMap<>();

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

    public void put(LocalDate date, Double value, ExtraPaymentsMap.INTERVAL inter, AmountOrTerm aot) {
        double interval;
        switch (inter) {
            case NONE:
                interval = 0;
                break;
            case ONE_MONTH:
                interval = 1;
                break;
            case THREE_MONTH:
                interval = 3;
                break;
            case SIX_MONTH:
                interval = 6;
                break;
            case YEAR:
                interval = 12;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + inter);
        }
        HashMap<AmountOrTerm, double[]> temp = new HashMap<>();
        temp.put(aot, new double[]{interval, value});
        result.put(date, temp);
    }

    public Map.Entry<LocalDate, HashMap<AmountOrTerm, Double>> pollFirstEntry() {
        LocalDate date = result.firstEntry().getKey();
        HashMap<AmountOrTerm, double[]> tempValue = result.firstEntry().getValue();
        AmountOrTerm aot = tempValue.containsKey(AmountOrTerm.AMOUNT_REDUCE) ? AmountOrTerm.AMOUNT_REDUCE : AmountOrTerm.TERM_REDUCE;
        TreeMap<LocalDate, HashMap<AmountOrTerm, Double>> res = new TreeMap<>();;
        HashMap<AmountOrTerm, Double> resValue = new HashMap<>();

        putToResultByInterval(aot, date, tempValue);
        resValue.put(aot, tempValue.get(aot)[1]);
        res.put(date, resValue);
         if (result.firstEntry().getValue().size() > 1) {
             result.firstEntry().getValue().remove(aot);
         } else result.pollFirstEntry();

        return res.pollFirstEntry();
    }

    private void putToResultByInterval(AmountOrTerm aot, LocalDate date, HashMap<AmountOrTerm, double[]> tempValue) {
        int interval = (int) tempValue.get(aot)[0];
        if (tempValue.get(aot)[0] != 0) {
            if (result.containsKey(date.plusMonths(interval))) {
                result.get(date.plusMonths(interval)).putAll(tempValue);
            } else {
                result.put(date.plusMonths(interval), tempValue);
            }
        }
    }
    public LocalDate firstKey() {
        return result.firstKey();
    }
    public int size() {
        return result.size();
    }
}
