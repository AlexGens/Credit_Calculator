package com.example.extraPayments;

import java.util.Calendar;

public class TermMap implements ExtraPayments {



    @Override
    public boolean put(Integer key, Calendar date, Integer value, ExtraPaymentsMap.INTERVAL interval) {
        return false;
    }

    @Override
    public int getNext() {
        return 0;
    }
}
