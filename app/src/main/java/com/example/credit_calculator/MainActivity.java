package com.example.credit_calculator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.Calendar;


public class MainActivity extends Activity {

    private EditText payoutDurationEdTx;
    private EditText creditSumEdTx;
    private EditText percentEdTx;
    private int creditSum;
    private double percent;
    private TextView result;
    private int payoutDuration;
    private TextView calculation;
    private Toast toast;
    private View rectangleView;
    private int years;
    private int months;
    private int days;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        creditSumEdTx = (EditText) findViewById(R.id.credit_Sum_id);
        CreditType creditType = CreditType.ANNUITY;
        calculation = (TextView) findViewById(R.id.calculation_id);
        payoutDurationEdTx = (EditText) findViewById(R.id.how_long_id);
        percentEdTx = (EditText) findViewById(R.id.percent_id);


        calculation.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               try {
                   creditSum = Integer.parseInt(creditSumEdTx.getText().toString());
               } catch (Exception e) {
                   toast = Toast.makeText(context, "Введите необходимую сумму",Toast.LENGTH_LONG );
                   toast.show();
                   return;
               }
               try {
                   percent = Double.parseDouble(percentEdTx.getText().toString()) / 100;
               } catch (Exception e) {
                   toast = Toast.makeText(context, "Введите процентную ставку",Toast.LENGTH_LONG );
                   toast.show();
                   return;
               }
               try {
                   payoutDuration = Integer.parseInt(payoutDurationEdTx.getText().toString());
               } catch (Exception e) {
                   toast = Toast.makeText(context, "Введите срок кредитования",Toast.LENGTH_LONG );
                   toast.show();
                   return;
               }
               CalcAnnuitet calculator = new CalcAnnuitet(creditSum, percent, payoutDuration);
           result.setText(String.valueOf(calculator.getAnnuitetPay()));




           }
       });



    }




}