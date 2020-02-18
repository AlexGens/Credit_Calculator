package com.example.credit_calculator;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.TreeMap;


public class GraphActivity extends Activity {
private TreeMap<Calendar, HashMap<GRAPH_COLUMN, Double>> map;
private GridLayout gridLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        map = ( TreeMap<Calendar, HashMap<GRAPH_COLUMN, Double>>) getIntent().getParcelableExtra("pureGraph");
//        gridLayout = (GridLayout) findViewById(R.id.graphXML);
//        gridLayout.setColumnCount(4);
//        gridLayout.setRowCount(map.size() + 1);
//        TextView text = new TextView(this);
//        text.setText("Колонка");
//        gridLayout.addView(text);

    }
}
