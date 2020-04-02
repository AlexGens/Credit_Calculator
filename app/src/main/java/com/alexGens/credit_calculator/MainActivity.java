package com.alexGens.credit_calculator;
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

import com.alexGens.calculation.Calculation;
import com.alexGens.calculation.CredSumAnnuity;
import com.alexGens.calculation.GraphColumn;
import com.alexGens.calculation.MonthPayAnnuity;
import com.alexGens.calculation.MonthPayDifferentiated;
import com.alexGens.calculation.PayoutDurAnnuity;
import com.alexGens.extraPayments.ExtraPaymentsMap;
import com.alexGens.extraPayments.ResultMap;
import com.alexGens.results.ResultsActivity;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TreeMap;

import static com.alexGens.credit_calculator.CreditType.ANNUITY;
import static com.alexGens.credit_calculator.CreditType.DIFFERENTIETED;
import static com.alexGens.credit_calculator.NeedToFind.CREDIT_SUM;
import static com.alexGens.credit_calculator.NeedToFind.MONTH_PAY;
import static com.alexGens.credit_calculator.NeedToFind.PAYOUT_DURATION;


public class MainActivity extends Activity   implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    // Views
    private EditText payoutDurationEdTx;
    private Spinner payoutDurationSpinner;
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
    private TextView addExtraPayTxvw;
    private LinearLayout forExtraPaymentsLnLo;
    private EditText extraPaymentDateEdTx;
    private LinearLayout paymentDataLnLo;
    private DatePickerDialog.OnDateSetListener listener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    years = year;
                    months = monthOfYear + 1;
                    days = dayOfMonth;
                    updateDisplay();
                }
            };
    private DatePickerDialog.OnDateSetListener extraPaymentDataListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            updateExtraPaymentDate(year, month + 1, dayOfMonth);

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
    private NeedToFind needToFind;
    private ArrayList<ArrayList<View> > extraPaymentsViews = new ArrayList<>();
    public static TreeMap<LocalDate, HashMap<GraphColumn, ArrayList<Double>>> resultGraph;

    public interface StartDataListener {
        void startDataListener(ArrayList<String> data);
    }
    private StartDataListener dataListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        setVariables();
        addExtraPayments();
        calculate();
    }

    private void setVariables() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
     //   creditSumEdTx = (EditText) findViewById(R.id.credit_Sum_id);
        creditType = ANNUITY;
        calculation = (TextView) findViewById(R.id.calculation_id);
        spinner = (Spinner) findViewById(R.id.spinner);
        // <--TODO разобраться с лишними действиями -->
        addExtraPayTxvw = (TextView) findViewById(R.id.main_add_extra_pay_id);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.spinner_strings, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        allLayoutLnLo = (LinearLayout) findViewById(R.id.all_layout_id);
        needToFind = MONTH_PAY;
       // <-- TODO Убрать tex -->
        LayoutInflater inflater = getLayoutInflater();
        monthPaylayout = (LinearLayout)inflater.inflate(R.layout.month_pay_layout, (ViewGroup) findViewById(R.id.payment_data_layout_id), false);
        payoutDurLayout = (LinearLayout)inflater.inflate(R.layout.payout_duration_layout, (ViewGroup) findViewById(R.id.payout_dur_payment_data_layout_id), false);
        credSumLayout = (LinearLayout)inflater.inflate(R.layout.credit_sum_layout, (ViewGroup) findViewById(R.id.cred_sum_payment_data_layout_id), false);
        forExtraPaymentsLnLo = (LinearLayout) findViewById(R.id.main_for_extra_payments_layout);
        startDate = LocalDate.now();
        years = startDate.getYear();
        months = startDate.getMonthOfYear();
        days = startDate.getDayOfMonth();
        allLayoutLnLo.addView(monthPaylayout);
        thisLayout = monthPaylayout;
        setLayoutVariables();
    }

    private void setLayoutVariables() {
        String temp;
        switch (needToFind) {
            case MONTH_PAY:
                temp = payoutDurationEdTx == null ? null : payoutDurationEdTx.getText().toString();
                payoutDurationEdTx = (EditText) findViewById(R.id.month_pay_lay_payout_duration_id);
                if (temp == null) {} else {
                    payoutDurationEdTx.setText(temp);
                }
                payoutDurationSpinner = (Spinner) findViewById(R.id.month_pay_lay_spinner_payout_dur_id);
                temp = creditSumEdTx == null ? null : creditSumEdTx.getText().toString();
                creditSumEdTx = (EditText) findViewById(R.id.month_pay_lay_credit_sum_id);
                if (temp == null) {} else {
                    creditSumEdTx.setText(temp);
                }
                temp = percentEdTx == null ? null : percentEdTx.getText().toString();
                percentEdTx = (EditText) findViewById(R.id.month_pay_lay_percent_id);
                if (temp == null) {} else {
                    percentEdTx.setText(temp);
                }
                temp = dateOfStartEdtx == null ? null : dateOfStartEdtx.getText().toString();
                dateOfStartEdtx = (EditText) findViewById(R.id.month_pay_lay_date_of_start_id);
                if (temp == null) {} else {
                    dateOfStartEdtx.setText(temp);
                }
                break;
            case PAYOUT_DURATION:
                temp = creditSumEdTx == null ? null : creditSumEdTx.getText().toString();
                creditSumEdTx = (EditText) findViewById(R.id.payout_lay_credit_sum_id);
                if (temp == null) {} else {
                    creditSumEdTx.setText(temp);
                }
                temp = percentEdTx == null ? null : percentEdTx.getText().toString();
                percentEdTx = (EditText) findViewById(R.id.payout_lay_percent_id);
                if (temp == null) {} else {
                    percentEdTx.setText(temp);
                }
                temp = dateOfStartEdtx == null ? null : dateOfStartEdtx.getText().toString();
                dateOfStartEdtx = (EditText) findViewById(R.id.payout_lay_date_of_start_id);
                if (temp == null) {} else {
                    dateOfStartEdtx.setText(temp);
                }
                temp = monthPayEdTx == null ? null : monthPayEdTx.getText().toString();
                monthPayEdTx = (EditText) findViewById(R.id.payout_lay_month_pay_id);
                if (temp == null) {} else {
                    monthPayEdTx.setText(temp);
                }
                break;
            case CREDIT_SUM:
                temp = payoutDurationEdTx == null ? null : payoutDurationEdTx.getText().toString();
                payoutDurationEdTx = (EditText) findViewById(R.id.cred_sum_lay_payout_duration_id);
                if (temp == null) {} else {
                    payoutDurationEdTx.setText(temp);
                }
                payoutDurationSpinner = (Spinner) findViewById(R.id.cred_sum_lay_spinner_payout_dur_id);
                temp = percentEdTx == null ? null : percentEdTx.getText().toString();
                percentEdTx = (EditText) findViewById(R.id.cred_sum_lay_percent_id);
                if (temp == null) {} else {
                    percentEdTx.setText(temp);
                }
                temp = dateOfStartEdtx == null ? null : dateOfStartEdtx.getText().toString();
                dateOfStartEdtx = (EditText) findViewById(R.id.cred_sum_lay_date_of_start_id);
                if (temp == null) {} else {
                    dateOfStartEdtx.setText(temp);
                }
                temp = monthPayEdTx == null ? null : monthPayEdTx.getText().toString();
                monthPayEdTx = (EditText) findViewById(R.id.cred_sum_lay_month_pay_id);
                if (temp == null) {} else {
                    monthPayEdTx.setText(temp);
                }
                break;
        }
    }

    private void calculate() {
        calculation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hasAllDataToCalc()){}
                else {return;}
                updateDisplay();
                Calculation pureGraph;
                switch (needToFind) {
                    case MONTH_PAY:
                        if (creditType == ANNUITY) {
                            pureGraph = new MonthPayAnnuity(payoutDuration, creditSum, percent, startDate);
                            break;
                        }
                        else if (creditType == DIFFERENTIETED) {
                            pureGraph = new MonthPayDifferentiated(payoutDuration, creditSum, percent, startDate);
                            break;
                        }
                    case PAYOUT_DURATION:
                        pureGraph = new PayoutDurAnnuity(monthPay, creditSum, percent, startDate);
                        break;
                    case CREDIT_SUM:
                        pureGraph = new CredSumAnnuity(payoutDuration, monthPay, percent, startDate);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + needToFind);
                }

                if (extraPaymentsViews.size() == 0) {}
                else if (putDataToExtraPaymentMap(pureGraph)){}
                else {return;}
                resultGraph = new ResultMap(pureGraph, extraPaymentsMap,  startDate, creditSum, monthPay, percent, payoutDuration).getResultGraph();
           //     dataListener.startDataListener(startData());
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ResultsActivity.class);
                startActivity(intent);
            }

        });
    }

    private ArrayList<String> startData() {
        ArrayList<String> startData = new ArrayList<>();
        startData.add(startDate.toString("dd.MM.yyyy"));
        startData.add(String.format("%,d", creditSum));
        startData.add(String.format("%.2f", percent) + "%");
        if (payoutDuration < 5 && (payoutDurationSpinner.getSelectedItemPosition()) == 0) {
            startData.add(String.valueOf(payoutDuration) + " " + getResources().getString(R.string.payout_duration_months_less_5));
        } else if (payoutDuration >= 5) {
            startData.add(String.valueOf(payoutDuration) + " " + payoutDurationSpinner.getSelectedItem());
        } else if (payoutDuration == 1 && (payoutDurationSpinner.getSelectedItemPosition()) == 1) {
            startData.add(String.valueOf(payoutDuration) + " " + getResources().getString(R.string.payout_duration_years_1));
        } else if (payoutDuration > 1 && payoutDuration < 5 && (payoutDurationSpinner.getSelectedItemPosition()) == 1) {
            startData.add(String.valueOf(payoutDuration) + " " + getResources().getString(R.string.payout_duration_years_less_5));
        }
        return startData;
    }

    private boolean putDataToExtraPaymentMap(Calculation pureGraph) {
        /**
         * Метод работает с данными, введёнными в views для частичных погашений.
         * Если в каком-то из созданных view есть пустые строки,
         * то метод выводит на экран соответствующее сообщение и возвращает false.
         * Если все строки заполнены, то метод проверяет значения и добавляет их в extraPaymentMap.
         */
        for (ArrayList<View> list  : extraPaymentsViews
             ) {
            if (list.get(6) == null) {
                toast = Toast.makeText(context, "Введите сумму частичного погашения",Toast.LENGTH_LONG );
                toast.show();
                return false;
            } else if (list.get(5) == null) {
                toast = Toast.makeText(context, "Введите дату частичного погашения",Toast.LENGTH_LONG );
                toast.show();
                return false;
            }
        }
        extraPaymentsMap = new ExtraPaymentsMap(pureGraph);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");
        for ( ArrayList<View> list  : extraPaymentsViews
             ) {
            EditText temp;
            temp = (EditText) list.get(5);
            LocalDate localDate = LocalDate.parse(temp.getText().toString(), formatter);

            temp = (EditText) list.get(6);
            double value = Double.parseDouble(temp.getText().toString());

            RadioButton rb = (RadioButton) list.get(2);
            AmountOrTerm aot = rb.isChecked() ? AmountOrTerm.AMOUNT_REDUCE : AmountOrTerm.TERM_REDUCE;

            Spinner sp = (Spinner) list.get(3);
            ExtraPaymentsMap.INTERVAL interval;
            switch (sp.getSelectedItemPosition()) {
                case 1 : interval = ExtraPaymentsMap.INTERVAL.ONE_MONTH; break;
                case 2 : interval = ExtraPaymentsMap.INTERVAL.THREE_MONTH; break;
                case 3 : interval = ExtraPaymentsMap.INTERVAL.SIX_MONTH; break;
                case 4 : interval = ExtraPaymentsMap.INTERVAL.YEAR; break;
                default: interval = ExtraPaymentsMap.INTERVAL.NONE; break;
            }
            extraPaymentsMap.put(localDate, value, interval, aot);
        }
        return true;
    }

    private boolean hasAllDataToCalc() {
         /**
         Метод выполняется перед началом расчёта и проверяет все ли строки заполнены.
          Если какая-то строка оказывается не заполнена, метод возвращает false.
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
                if(payoutDurationSpinner.getSelectedItemPosition() == 0) {
                    payoutDuration = Integer.parseInt(payoutDurationEdTx.getText().toString());
                } else payoutDuration = Integer.parseInt(payoutDurationEdTx.getText().toString()) * 12;
            // TODO сделать срок не меньше трёх месяцев
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
        private void addExtraPayments() {
            addExtraPayTxvw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = getLayoutInflater();
                    View view = (LinearLayout) inflater.inflate(R.layout.extra_payments_add, (ViewGroup) findViewById(R.id.extra_pay_add_all_id), false);

                    forExtraPaymentsLnLo.addView(view);
                    ArrayList<View> temp = new ArrayList<>();
                    temp.add(view);
                    view.addChildrenForAccessibility(temp);
                    extraPaymentsViews.add(temp);
                    int x = 5;


                }
            }); {

            };
        }

    @Override
    public void onClick(View view) {
        DatePickerDialog dialog = new DatePickerDialog(
                this, listener, years, months - 1, days );
        dialog.show();
    }
    public void extraPaymentDateClick(View view) {
        extraPaymentDateEdTx = (EditText) view;
        DatePickerDialog dialog = new DatePickerDialog(this, extraPaymentDataListener, LocalDate.now().getYear(), LocalDate.now().getMonthOfYear(), LocalDate.now().getDayOfYear());
        dialog.show();
    }
    private void updateExtraPaymentDate(int year, int month, int dayOfMonth) {
        StringBuilder temp = new StringBuilder();
        temp.append( String.format("%02d" , dayOfMonth)).append(".")
            .append( String.format("%02d" , month)).append(".")
            .append( String.format("%02d" , year));
        extraPaymentDateEdTx.setText(temp);

    }

    private void updateDisplay() {
        startDate = LocalDate.parse(years + "-" + months + "-" + days);;
        dateOfStartEdtx.setText(
                new StringBuilder()
                        .append(months).append("-")
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
    public void closeClick (View view) {
        //  TODO Сделать закрывашку
        LinearLayout ln = (LinearLayout) extraPaymentsViews.get(0).get(0);
        ln.removeAllViewsInLayout();

    }



}