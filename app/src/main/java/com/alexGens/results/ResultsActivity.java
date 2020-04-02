package com.alexGens.results;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.GridLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.alexGens.calculation.GraphColumn;
import com.alexGens.credit_calculator.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.HashMap;
import java.util.TreeMap;


@SuppressLint("Registered")
public class ResultsActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager viewPager;
    PageAdapter pageAdapter;
    TabLayout tabLayout;
private GridLayout gridLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);

        toolbar=(Toolbar) findViewById(R.id.result_toolbar);
        viewPager=(ViewPager) findViewById(R.id.result_viewpager);
        tabLayout=(TabLayout) findViewById(R.id.result_tablayout);
        setSupportActionBar(toolbar);

       // Fragment fr = getSupportFragmentManager().findFragmentById(R.layout.fragment_show_results);
        Resources resources = getResources();
        pageAdapter = new PageAdapter(getSupportFragmentManager());
        pageAdapter.addFragment(new ShowResultsFragment(), resources.getString(R.string.fragment_show_results) );
        pageAdapter.addFragment(new GraphFragment(), resources.getString(R.string.fragment_graph));
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        viewPager.setAdapter(pageAdapter);

        tabLayout.setupWithViewPager(viewPager);
        // TODO  сделать вывод результатов в fragment show results, ссылка с материалом
        // https://startandroid.ru/ru/uroki/vse-uroki-spiskom/176-urok-106-android-3-fragments-vzaimodejstvie-s-activity.html


    }
}
