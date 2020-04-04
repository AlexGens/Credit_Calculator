package com.alexGens.results;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.alexGens.calculation.GraphColumn;
import com.alexGens.credit_calculator.MainActivity;
import com.alexGens.credit_calculator.R;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Context context;
    private TableLayout resultGraph;
    private View v;
    private TreeMap<Integer, HashMap<GraphColumn, String>> resultMap;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public GraphFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment graphFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static GraphFragment newInstance(String param1, String param2) {
        GraphFragment fragment = new GraphFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @SuppressLint({"Упало на addTabRow", "DefaultLocale"})
    private void addTableRow(Map.Entry<Integer, HashMap<GraphColumn, String>> entry) {
        Context themedContext = new ContextThemeWrapper(context, R.style.graph_text);

            TextView date = new TextView(themedContext);
            date.setText(entry.getValue().get(GraphColumn.DATE));
            TableRow tableRow = new TableRow(context);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            TextView basicPay =  new TextView(themedContext);
            basicPay.setText(entry.getValue().get(GraphColumn.BASIC_PAYMENT));

            TextView percents = new TextView(themedContext);
            percents.setText(entry.getValue().get(GraphColumn.PERCENTS));

            TextView fullPay = new TextView(themedContext);
            fullPay.setText(entry.getValue().get(GraphColumn.FULL_PAYMENT));

            TextView credSum = new TextView(themedContext);
            credSum.setText(entry.getValue().get(GraphColumn.FULL_CRED_SUM));

            tableRow.addView(date, 0);
            tableRow.addView(basicPay, 1);
            tableRow.addView(percents,2 );
            tableRow.addView(fullPay, 3);
            tableRow.addView(credSum, 4);
            resultGraph.addView(tableRow);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_graph, container, false);
        resultGraph =(TableLayout) v.findViewById(R.id.result_graph_table);
        resultMap = MainActivity.resultGraph;
        context = v.getContext();
        for (Map.Entry<Integer, HashMap<GraphColumn, String>> entry : resultMap.entrySet()
        ) {
            addTableRow(entry);
        }
        return v;
    }


}
