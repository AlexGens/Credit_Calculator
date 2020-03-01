package com.example.extraPayments;


import com.example.credit_calculator.AmountOrTerm;

import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

public class ExtraPaymentsMap  {
    private  TreeMap<Calendar, Integer> values;
    private  TreeMap<Calendar, AmountOrTerm> amountOrTerm;
    private  Calendar startDate;
    private  Calendar lastPay;
    public enum INTERVAL {NONE, ONE_MONTH, THREE_MONTH, SIX_MONTH, YEAR}
    private static ExtraPaymentsMap instance;


    public ExtraPaymentsMap(Calendar startDate, int payoutDuration) {
        this.startDate = startDate;
        this.lastPay.set(Calendar.YEAR, startDate.get(Calendar.YEAR));
        this.lastPay.set(Calendar.MONTH, startDate.get(Calendar.MONTH) + payoutDuration);
        this.lastPay.set(Calendar.DAY_OF_MONTH, startDate.get(Calendar.DAY_OF_MONTH));
    }

        /*
    Если частичное погашение идёт с периодичностью, заносим его в список несколько раз, согласно периоду.
    В конце удаляем лишние платежи, которые вышли за пределы графика.
    В случае совпадения дат частичных погашений их суммы складываются, а стратегия дальнейшего расчёта кредита
    остаётся, которая была принята раньше.
         */
    public boolean put(Calendar date, Integer value, ExtraPaymentsMap.INTERVAL interval, AmountOrTerm aot) {
        try {
            if (interval != INTERVAL.NONE) {
                while (date.before(lastPay)) {
                    if (values.containsKey(date)) {
                        values.put(date, values.get(date) + value);
                        switch (interval) {
                            case ONE_MONTH   : date.add(Calendar.MONTH,1); break;
                            case THREE_MONTH : date.add(Calendar.MONTH,3); break;
                            case SIX_MONTH   : date.add(Calendar.MONTH,6); break;
                            case YEAR        : date.add(Calendar.MONTH,12); break;
                        }
                    }  else {
                        values.put(date, value);
                        amountOrTerm.put(date, aot);
                        switch (interval) {
                            case ONE_MONTH   : date.add(Calendar.MONTH,1); break;
                            case THREE_MONTH : date.add(Calendar.MONTH,3); break;
                            case SIX_MONTH   : date.add(Calendar.MONTH,6); break;
                            case YEAR        : date.add(Calendar.MONTH,12);break;
                        }
                    }
                }
            } else {
                values.put(date, value);
                amountOrTerm.put(date, aot);
            }
            for (Map.Entry<Calendar, Integer> entry: values.entrySet()
                 ) {
                if (entry.getKey().after(lastPay)) {
                    values.remove(entry.getKey());
                    amountOrTerm.remove(entry.getKey());
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public TreeMap getAllValues() {
            return values;
    }
    public int getValue(Calendar key) {
        return values.get(key);
    }
    public AmountOrTerm getAoT(Calendar key) {
        return amountOrTerm.get(key);
    }

}
