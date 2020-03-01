package com.example.credit_calculator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.calculation.CredSumAnnuit;
import com.example.calculation.GraphColumn;
import com.example.calculation.MonthPayAnnuit;
import com.example.calculation.MonthPayDifference;
import com.example.calculation.PayoutDurAnnuit;
import com.example.extraPayments.ExtraPaymentsMap;
import com.example.results.GraphActivity;

import org.joda.time.LocalDate;

import java.util.Calendar;
import java.util.HashMap;
import java.util.TreeMap;

import static com.example.credit_calculator.CreditType.ANNUITY;
import static com.example.credit_calculator.CreditType.DIFFERENTIETED;
import static com.example.credit_calculator.NeedToFind.CREDIT_SUM;
import static com.example.credit_calculator.NeedToFind.MONTH_PAY;
import static com.example.credit_calculator.NeedToFind.PAYOUT_DURATION;


public class MainActivity extends Activity   implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    // Views
    private EditText payoutDurationEdTx;
    private Spinner payoutDurationSpin;
    private EditText creditSumEdTx;
    private EditText percentEdTx;
    private EditText dateOfStartEdtx;
    private EditText monthPayEdTx;
    private Toast toast;
    private TextView calculation;
    private Spinner spinner;
    private LinearLayout allLayoutLnLo;
    private LinearLayout monthPaylayout;
    private LinearLayout payoutDurLayout;
    private LinearLayout credSumLayout;
    private LinearLayout thisLayout;
    private DatePickerDialog.OnDateSetListener listener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    years = year;
                    months = monthOfYear;
                    days = dayOfMonth;
                    updateDisplay();
                }
            };


    // Остальные поля класса
    private int creditSum = 0;
    private double monthPay = 0;
    private double percent;
    private int payoutDuration = 0;
    private int years;
    private int months;
    private int days;
    private Context context;
    private CreditType creditType;
    private ExtraPaymentsMap extraPaymentsMap;
    private HashMap<Calendar, Integer> extraPayments;
    private LocalDate startDate;
    private LinearLayout paymentDataLnLo;
    private NeedToFind needToFind;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        setVariables();
        calculate();
    }








    private void calculate() {
        calculation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if (hasAllDataToCalc()){}
               else {return;}
               updateDisplay();
               TreeMap<LocalDate, HashMap<GraphColumn, Double>> map;                                 // Для передачи данных в следующий Activity
               switch (needToFind) {
                   case MONTH_PAY:
                       if (creditType == ANNUITY) {
                           MonthPayAnnuit monthPayAnnuit = new MonthPayAnnuit(payoutDuration, creditSum, percent, startDate);
                           map = monthPayAnnuit.getPureGraph();
                           break;
                       }
                       else if (creditType == DIFFERENTIETED) {
                           MonthPayDifference monthPayDifference = new MonthPayDifference(payoutDuration, creditSum, percent, startDate);
                           map = monthPayDifference.getPureGraph();
                           break;
                       }
                   case PAYOUT_DURATION:
                       PayoutDurAnnuit payoutDurAnnuit = new PayoutDurAnnuit(monthPay, creditSum, percent, startDate);
                       map = payoutDurAnnuit.getPureGraph();
                       break;
                   case CREDIT_SUM:
                       CredSumAnnuit credSumAnnuit = new CredSumAnnuit(payoutDuration, monthPay, percent, startDate);
                       map = credSumAnnuit.getPureGraph();
                       break;
                   default:
                       throw new IllegalStateException("Unexpected value: " + needToFind);
               }


                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), GraphActivity.class);
                    intent.putExtra("pureGraph", map);
                    startActivity(intent);






            }
        });
    }

    private boolean hasAllDataToCalc() {
         /*
         Проверяем указана ли:
             1. необходимая сумма
             2. процентная ставка
             3. срок кредитования
          Если не указана, расчёт не выполняется.
          */
        try {
            if (needToFind != CREDIT_SUM) {
                 creditSum = Integer.parseInt(creditSumEdTx.getText().toString());
            }
        } catch (Exception e) {
            toast = Toast.makeText(context, "Введите необходимую сумму",Toast.LENGTH_LONG );
            toast.show();
            return false;
        }
        try {
            percent = Double.parseDouble(percentEdTx.getText().toString()) / 100;
        } catch (Exception e) {
            toast = Toast.makeText(context, "Введите процентную ставку",Toast.LENGTH_LONG );
            toast.show();
            return  false;
        }
        try {
            if (needToFind != PAYOUT_DURATION) {
                payoutDuration = Integer.parseInt(payoutDurationEdTx.getText().toString());
            }
        } catch (Exception e) {
            toast = Toast.makeText(context, "Введите срок кредитования",Toast.LENGTH_LONG );
            toast.show();
            return  false;
        }
        try {
            if (needToFind != MONTH_PAY) {
                monthPay = Integer.parseInt(monthPayEdTx.getText().toString());
            }
        } catch (Exception e) {
            toast = Toast.makeText(context, "Введите сумму платежа",Toast.LENGTH_LONG );
            toast.show();
            return  false;
        }
        return true;
    }



    private void setVariables() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
     //   creditSumEdTx = (EditText) findViewById(R.id.credit_Sum_id);
        creditType = ANNUITY;
        calculation = (TextView) findViewById(R.id.calculation_id);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.spinner_strings, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        allLayoutLnLo = (LinearLayout) findViewById(R.id.all_layout_id);
        startDate = LocalDate.parse(years + "-" + months + "-" + days);
        needToFind = MONTH_PAY;
        LayoutInflater inflater = getLayoutInflater();
        monthPaylayout = (LinearLayout)inflater.inflate(R.layout.month_pay_layout, (ViewGroup) findViewById(R.id.payment_data_layout_id), false);
        payoutDurLayout = (LinearLayout)inflater.inflate(R.layout.payout_duration_layout, (ViewGroup) findViewById(R.id.payout_dur_payment_data_layout_id), false);
        credSumLayout = (LinearLayout)inflater.inflate(R.layout.credit_sum_layout, (ViewGroup) findViewById(R.id.cred_sum_payment_data_layout_id), false);
        startDate = LocalDate.now();
        //LayoutInflater li = LayoutInflater.from(this);
      //  MonthPayLayout.getInstance(context);
     //   TextView hello = new TextView(context);
       // hello.setText("hello");
        //hello.setTextSize(50);
        allLayoutLnLo.addView(monthPaylayout);
        thisLayout = monthPaylayout;
        setLayoutVariables();
        //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //hello.setLayoutParams(layoutParams);
      //  creditSumEdTx.setText(String.valueOf(250000));   // ДЛЯ ТЕСТА, ПОТОМ УБРАТЬ!!!
     //   percentEdTx.setText("18"); // ДЛЯ ТЕСТА, ПОТОМ УБРАТЬ!!!
     //   payoutDurationEdTx.setText("30");// ДЛЯ ТЕСТА, ПОТОМ УБРАТЬ!!!
    }

    @Override
    public void onClick(View view) {
        DatePickerDialog dialog = new DatePickerDialog(
                this, listener, years, months, days );
        dialog.show();
    }
    private void updateDisplay() {
        startDate = LocalDate.parse(years + "-" + months + "-" + days);;
        dateOfStartEdtx.setText(
                new StringBuilder()
                        .append(months + 1).append("-")
                        .append(days).append("-")
                        .append(years).append(" "));
    }
    public void credTypeClick(View view) {
        RadioButton rb = (RadioButton) view;
        changeCreditType(rb.getTag().toString());
    }
    private void changeCreditType(String type) {
        if (type.equals("annuitetXML")) {
            creditType = ANNUITY;
        }
        else if (type.equals("differenceXML")) {
            creditType = CreditType.DIFFERENTIETED;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       switch (position) {
           case 0 : updateViews(monthPaylayout);
           needToFind = MONTH_PAY;
           setLayoutVariables(); break;
           case 1 : updateViews(payoutDurLayout);
           needToFind = PAYOUT_DURATION;
           setLayoutVariables(); break;
           case 2 : updateViews(credSumLayout);
           needToFind = CREDIT_SUM;
           setLayoutVariables(); break;
       }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void updateViews(LinearLayout forUpdate) {
        if (forUpdate.equals(thisLayout)) {}
        else {
            allLayoutLnLo.removeView(thisLayout);
            allLayoutLnLo.addView(forUpdate);
            thisLayout = forUpdate;
        }
    }
    private void setLayoutVariables() {
        switch (needToFind) {
            case MONTH_PAY:
                 payoutDurationEdTx = (EditText) findViewById(R.id.month_pay_lay_payout_duration_id);
                 payoutDurationSpin = (Spinner) findViewById(R.id.month_pay_lay_spinner_payout_dur_id);
                 creditSumEdTx = (EditText) findViewById(R.id.month_pay_lay_credit_sum_id);
                 percentEdTx = (EditText) findViewById(R.id.month_pay_lay_percent_id);
                 dateOfStartEdtx = (EditText) findViewById(R.id.month_pay_lay_date_of_start_id);
                 break;
            case PAYOUT_DURATION:
                creditSumEdTx = (EditText) findViewById(R.id.payout_lay_credit_sum_id);
                percentEdTx = (EditText) findViewById(R.id.payout_lay_percent_id);
                dateOfStartEdtx = (EditText) findViewById(R.id.payout_lay_date_of_start_id);
                monthPayEdTx = (EditText) findViewById(R.id.payout_lay_month_pay_id);
                break;
            case CREDIT_SUM:
                payoutDurationEdTx = (EditText) findViewById(R.id.cred_sum_lay_payout_duration_id);
                payoutDurationSpin = (Spinner) findViewById(R.id.cred_sum_lay_spinner_payout_dur_id);
                percentEdTx = (EditText) findViewById(R.id.cred_sum_lay_percent_id);
                dateOfStartEdtx = (EditText) findViewById(R.id.cred_sum_lay_date_of_start_id);
                monthPayEdTx = (EditText) findViewById(R.id.cred_sum_lay_month_pay_id);
                break;
        }
    }
}