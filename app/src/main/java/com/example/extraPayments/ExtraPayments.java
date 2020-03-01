package com.example.extraPayments;

import com.example.credit_calculator.AmountOrTerm;

import java.util.Calendar;
import java.util.TreeMap;

public interface ExtraPayments {
    TreeMap<Calendar, Integer> values = new TreeMap<>();
    Calendar startDate = Calendar.getInstance();
    Calendar lastPay = Calendar.getInstance();
    enum INTERVAL {NONE, ONE_MONTH, THREE_MONTH, SIX_MONTH, YEAR}


    boolean put(Calendar date, Integer value, ExtraPaymentsMap.INTERVAL interval);
    int getNext();
}
