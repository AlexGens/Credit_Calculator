package com.alexGens.extraPayments;


import com.alexGens.calculation.Calculation;
import com.alexGens.calculation.GraphColumn;
import com.alexGens.credit_calculator.AmountOrTerm;

import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ExtraPaymentsMap  {
    public enum INTERVAL {NONE, ONE_MONTH, THREE_MONTH, SIX_MONTH, YEAR}
    private TreeMap<LocalDate, HashMap<GraphColumn, Double>> pureGraph;
    private TreeMap<LocalDate, HashMap<AmountOrTerm, Double>> result = new TreeMap<>();


    public ExtraPaymentsMap(Calculation pureGraph) {
        this.pureGraph = pureGraph.getPureGraph();
    }

        /**
         * Класс создаёт TreeMap (result) и помещает в него значения сумм частичных погашений и стратегий перерасчёта кредита, отсортированных по дате.
         *
         *
         * Описание метода put:
         * Если частичное погашение идёт с периодичностью, заносим его в result несколько раз, согласно периоду.
         * В конце удаляем лишние платежи, которые вышли за пределы графика.
         * В случае совпадения дат частичных погашений, но различных стратегий перерасчёта кредита,
           в HashMap в словаре result заносятся оба значения.
         * В случае совпадения и дат, и стратегий перерасчёта, суммы складываются в одну.
         *
         */


    public boolean put(LocalDate date, Double value, ExtraPaymentsMap.INTERVAL interval, AmountOrTerm aot) {
        try {
            if (interval != INTERVAL.NONE) {
                while (date.compareTo(pureGraph.lastKey()) < 0) {
                    if (result.containsKey(date)) {
                        if (result.get(date).containsKey(aot)) {
                            result.get(date).put(aot, result.get(date).get(aot) + value);
                        }
                        result.get(date).put(aot, value);
                        switch (interval) {
                            case ONE_MONTH   : date.plusMonths(1); break;
                            case THREE_MONTH : date.plusMonths(3); break;
                            case SIX_MONTH   : date.plusMonths(6); break;
                            case YEAR        : date.plusMonths(12); break;
                        }
                    }  else {
                        HashMap<AmountOrTerm, Double> temp = new HashMap<>();
                        temp.put(aot, value);
                        result.put(date, temp);
                        switch (interval) {
                            case ONE_MONTH   : date.plusMonths(1); break;
                            case THREE_MONTH : date.plusMonths(3); break;
                            case SIX_MONTH   : date.plusMonths(6); break;
                            case YEAR        : date.plusMonths(12); break;
                        }
                    }
                }
            } else {
                HashMap<AmountOrTerm, Double> temp = new HashMap<>();
                temp.put(aot, value);
                result.put(date, temp);
            }
            for (Map.Entry<LocalDate,HashMap<AmountOrTerm, Double>> entry: result.entrySet()
                 ) {
                if (entry.getKey().compareTo(pureGraph.lastKey()) > 0) {
                    result.remove(entry.getKey());
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public TreeMap<LocalDate, HashMap<AmountOrTerm, Double>> getExtraPaymentsMap() {
            return result;
    }

}
