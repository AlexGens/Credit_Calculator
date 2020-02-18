package com.example.credit_calculator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TreeMap;


public class MainActivity extends Activity   implements View.OnClickListener{

    // Views
    private EditText payoutDurationEdTx;
    private EditText creditSumEdTx;
    private EditText percentEdTx;
    private EditText dateOfStartEdtx;
    private Toast toast;
    private TextView calculation;
    private Spinner spinner;
    private LinearLayout allLayoutLnLo;
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
    private Calendar startDate;
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

                if (creditType == CreditType.ANNUITY) {
                    if (monthPay == 0) {
                        MonthPayAnnuit monthPayAnnuit = new MonthPayAnnuit(payoutDuration, creditSum, percent, startDate);
                    }
                    else if (creditSum == 0) {
                        CredSumAnnuit credSumAnnuit = new CredSumAnnuit(payoutDuration, monthPay, percent, startDate);
                    }
                    else if (payoutDuration == 0) {
                        PayoutDurAnnuit payoutDurAnnuit = new PayoutDurAnnuit(monthPay, creditSum, percent, startDate);
                    }


                   // TreeMap<Calendar, HashMap<GRAPH_COLUMN, Double>> map = PureAnnuitet.getGraphMonthPay(payoutDuration, creditSum, percent, startDate);
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), GraphActivity.class);
                  //  intent.putExtra("pureGraph", map);
                    startActivity(intent);

                }
                else if (creditType == CreditType.DIFFERENTIETED) {
                    CalcDifference calculator = new CalcDifference();
                }




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
        boolean result;
        try {
            creditSum = Integer.parseInt(creditSumEdTx.getText().toString());
            result = true;
        } catch (Exception e) {
            toast = Toast.makeText(context, "Введите необходимую сумму",Toast.LENGTH_LONG );
            toast.show();
            result = false;
        }
        try {
            percent = Double.parseDouble(percentEdTx.getText().toString()) / 100;
            result = true;

        } catch (Exception e) {
            toast = Toast.makeText(context, "Введите процентную ставку",Toast.LENGTH_LONG );
            toast.show();
            result = false;
        }
        try {
            payoutDuration = Integer.parseInt(payoutDurationEdTx.getText().toString());
            result = true;

        } catch (Exception e) {
            toast = Toast.makeText(context, "Введите срок кредитования",Toast.LENGTH_LONG );
            toast.show();
            result = false;
        }
        return result;
    }



    private void setVariables() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
     //   creditSumEdTx = (EditText) findViewById(R.id.credit_Sum_id);
        creditType = CreditType.ANNUITY;
        calculation = (TextView) findViewById(R.id.calculation_id);
        spinner = (Spinner) findViewById(R.id.spinner);
        allLayoutLnLo = (LinearLayout) findViewById(R.id.all_layout_id);
      //  payoutDurationEdTx = (EditText) findViewById(R.id.payout_duration_id);
      //  percentEdTx = (EditText) findViewById(R.id.percent_id);
     //   dateOfStartEdtx = (EditText) findViewById(R.id.date_of_start_id);
      //  paymentDataLnLo = (LinearLayout) findViewById(R.id.PaymentDataLayout_id);
        startDate = Calendar.getInstance();
        years = startDate.get(Calendar.YEAR);
        months = startDate.get(Calendar.MONTH);
        days = startDate.get(Calendar.DAY_OF_MONTH);
        needToFind = NeedToFind.MONTH_PAY;

        View convertView = LayoutInflater.from(context).inflate(
                R.layout.basic_layout, allLayoutLnLo, false);
      //  MonthPayLayout.getInstance(context);
     //   TextView hello = new TextView(context);
       // hello.setText("hello");
        //hello.setTextSize(50);
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
        startDate.set(years , months, days );
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
            creditType = CreditType.ANNUITY;
        }
        else if (type.equals("differenceXML")) {
            creditType = CreditType.DIFFERENTIETED;
        }
    }
    private void updateViews() {
        LinearLayout layout = new LinearLayout(this);
    }
}