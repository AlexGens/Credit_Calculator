package com.example.results;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.GridLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.calculation.GraphColumn;
import com.example.credit_calculator.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.HashMap;
import java.util.TreeMap;


@SuppressLint("Registered")
public class GraphActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager viewPager;
    PageAdapter pageAdapter;
    TabLayout tabLayout;
private TreeMap<Calendar, HashMap<GraphColumn, Double>> map;
private GridLayout gridLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);

        toolbar=(Toolbar) findViewById(R.id.result_toolbar);
        viewPager=(ViewPager) findViewById(R.id.result_viewpager);
        tabLayout=(TabLayout) findViewById(R.id.result_tablayout);
        setSupportActionBar(toolbar);

        Resources resources = getResources();
        pageAdapter = new PageAdapter(getSupportFragmentManager());
        pageAdapter.addFragment(new showResultsFrag(), resources.getString(R.string.fragment_show_results) );
        pageAdapter.addFragment(new graphFrag(), resources.getString(R.string.fragment_graph));
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        viewPager.setAdapter(pageAdapter);

        tabLayout.setupWithViewPager(viewPager);

        map = getIntent().getParcelableExtra("pureGraph");
//        gridLayout = (GridLayout) findViewById(R.id.graphXML);
//        gridLayout.setColumnCount(4);
//        gridLayout.setRowCount(map.size() + 1);
//        TextView text = new TextView(this);
//        text.setText("Колонка");
//        gridLayout.addView(text);

    }
}
