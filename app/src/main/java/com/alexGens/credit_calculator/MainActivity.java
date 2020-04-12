package com.alexGens.credit_calculator;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.BaseInputConnection;
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

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.alexGens.calculation.Calculation;
import com.alexGens.calculation.GraphColumn;
import com.alexGens.extraPayments.ExtraPaymentsMap;
import com.alexGens.results.ResultsActivity;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.alexGens.credit_calculator.CreditType.ANNUITY;
import static com.alexGens.credit_calculator.CreditType.DIFFERENTIETED;
import static com.alexGens.credit_calculator.NeedToFind.CREDIT_SUM;
import static com.alexGens.credit_calculator.NeedToFind.MONTH_PAY;
import static com.alexGens.credit_calculator.NeedToFind.PAYOUT_DURATION;


public class MainActivity extends FragmentActivity   implements View.OnClickListener, AdapterView.OnItemSelectedListener {
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
    private LinearLayout fragmentContainerView;
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
    private double creditSum = 0;
    private double monthPay = 0;
    private double percent;
    private int payoutDuration = 0;
    private int years;
    private int months;
    private int days;
    private Context context;
    private CreditType creditType;
    private ExtraPaymentsMap extraPaymentsMap = new ExtraPaymentsMap();
    private HashMap<Calendar, Integer> extraPayments;
    private LocalDate startDate;
    private NeedToFind needToFind;
    public static TreeMap<Integer, HashMap<GraphColumn, String>> resultGraph;


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
        fragmentContainerView = (LinearLayout) findViewById(R.id.fragment_container_view);
        needToFind = MONTH_PAY;
       // <-- TODO Убрать tex -->
        LayoutInflater inflater = getLayoutInflater();
        monthPaylayout = (LinearLayout)inflater.inflate(R.layout.month_pay_layout, (ViewGroup) findViewById(R.id.payment_data_layout_id), false);
        payoutDurLayout = (LinearLayout)inflater.inflate(R.layout.payout_duration_layout, (ViewGroup) findViewById(R.id.payout_dur_payment_data_layout_id), false);
        credSumLayout = (LinearLayout)inflater.inflate(R.layout.credit_sum_layout, (ViewGroup) findViewById(R.id.cred_sum_payment_data_layout_id), false);
      //  forExtraPaymentsLnLo = (LinearLayout) findViewById(R.id.con);
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

                    creditSumEdTxFormat();
                }
                temp = percentEdTx == null ? null : percentEdTx.getText().toString();
                percentEdTx = (EditText) findViewById(R.id.month_pay_lay_percent_id);
                if (temp == null) {} else {
                    percentEdTx.setText(temp);
                    percentEdTxFormat();
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
                    creditSumEdTxFormat();
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

    private void percentEdTxFormat() {
        percentEdTx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
//            if (percentEdTx.getText().length() != 0) {
//                if ()
//            }
            }
        });
    }

    private void creditSumEdTxFormat() {
        final String[] credSum = {""};
        final int[] selector = {0};
        creditSumEdTx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @SuppressLint("DefaultLocale")
            @Override
            public void afterTextChanged(Editable s) {
                if (creditSumEdTx.getText().toString().length() != 0) {
                    if (creditSumEdTx.getText().toString().replaceAll("[\\s,]", "").equals(credSum[0].replaceAll("[\\s,]", "")) && !creditSumEdTx.getText().toString().equals(credSum[0]) && creditSumEdTx.getText().toString().length() <= 10) {
                        BaseInputConnection textFieldInputConnection = new BaseInputConnection(creditSumEdTx, true);
                        textFieldInputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                    }else if (!creditSumEdTx.getText().toString().equals(credSum[0]) && creditSumEdTx.getText().toString().length() <= 10) {
                        selector[0] = creditSumEdTx.getSelectionEnd();
                        credSum[0] = setThousandsSeparator(creditSumEdTx.getText().toString());
                        int separators = credSum[0].replaceAll(".*^,", "").length() - creditSumEdTx.getText().toString().replaceAll(".*^,", "").length();
                        selector[0] = credSum[0].equals("0") ? 1 : selector[0] + separators;
                        creditSumEdTx.setText(credSum[0]);
                    } else creditSumEdTx.setSelection(selector[0]);
                } else {
                    selector[0] = 0;
                    credSum[0] = "";
                }
            }
        });
    }

    private String setThousandsSeparator(String value) {
        String temp = value.replaceAll("[\\s,]", "");
        StringBuilder builder = new StringBuilder().append(temp).reverse();
        byte separators = 0;
        for (int i = 1; i <= temp.length() ; i++) {
            if ((i - 1) % 3 == 0 && i - 1 != 0) {
                builder.insert(i - 1 + separators, ",");
                separators++;
            }
        }
        return builder.reverse().toString();
    }

    private void calculate() {
        calculation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calculation pureGraph;
                boolean isTest = false;
                if (test(isTest)) {
                    DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");
                    // Входные данные
                    needToFind = MONTH_PAY;
                    creditType = ANNUITY;
                    creditSum = 250000;
                    payoutDuration = 36;
                    percent = 0.18;
                    startDate = LocalDate.parse("05.04.2020", formatter);
                    monthPay = 0;
                    //

                } else if (hasAllDataToCalc()) {
                updateDisplay();
                }
                else {return;}
                if (test(isTest)) {
                        extraPaymentsMap = new ExtraPaymentsMap();
                        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");

                            LocalDate localDate = LocalDate.parse("06.08.2020", formatter);
                            double value = 17000;
                            AmountOrTerm aot = AmountOrTerm.AMOUNT_REDUCE;
                            ExtraPaymentsMap.INTERVAL interval = ExtraPaymentsMap.INTERVAL.ONE_MONTH;
                            extraPaymentsMap.put(localDate, value, interval, aot);

                            localDate = LocalDate.parse("06.08.2020", formatter);
                            value = 8000;
                            aot = AmountOrTerm.TERM_REDUCE;
                            interval = ExtraPaymentsMap.INTERVAL.ONE_MONTH;
                            extraPaymentsMap.put(localDate, value, interval, aot);

                } else if (putDataToExtraPaymentMap()) {
                } else {
                    return;
                }
                switch (needToFind) {
                    case MONTH_PAY:
                        if (creditType == ANNUITY) {
                            pureGraph = new Calculation(creditSum, payoutDuration,  percent, startDate, extraPaymentsMap);
                            break;
                        } else if (creditType == DIFFERENTIETED) {
                            pureGraph = new Calculation(creditSum ,payoutDuration ,  percent, startDate, creditType, extraPaymentsMap);
                            break;
                        }
                    case PAYOUT_DURATION:
                        pureGraph = new Calculation(creditSum, monthPay, percent, startDate, extraPaymentsMap);
                        break;
                    case CREDIT_SUM:
                        pureGraph = new Calculation(payoutDuration, monthPay, percent, startDate, extraPaymentsMap);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + needToFind);
                }


                resultGraph = pureGraph.getPureGraph();
                //     dataListener.startDataListener(startData());
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ResultsActivity.class);
                startActivity(intent);


            }

        });
    }

    private boolean test(boolean b) {
        return b;
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

    /**
     * Метод работает с данными, введёнными в views для частичных погашений.
     * Если в каком-то из созданных view есть пустые строки,
     * то метод выводит на экран соответствующее сообщение и возвращает false.
     * Если все строки заполнены, то метод проверяет значения и добавляет их в extraPaymentMap.
     */
    private boolean putDataToExtraPaymentMap() {
       ArrayList<ArrayList<View> > extraPaymentsViews = new ArrayList<>();
        for (int i = 0; i < fragmentContainerView.getChildCount() ; i++) {
            ArrayList<View> list = new ArrayList<>();
            fragmentContainerView.getChildAt(i).addChildrenForAccessibility(list);
            extraPaymentsViews.add(list);
        }
        if (!checkExtraPaymentValues(extraPaymentsViews)){return false;}


        extraPaymentsMap = new ExtraPaymentsMap();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");
        for ( ArrayList<View> list  : extraPaymentsViews
             ) {
            EditText temp;
            temp = (EditText) list.get(2);
            LocalDate localDate = LocalDate.parse(temp.getText().toString(), formatter);

            temp = (EditText) list.get(1);
            double value = Double.parseDouble(temp.getText().toString());

            RadioButton rb = (RadioButton) list.get(5);
            AmountOrTerm aot = rb.isChecked() ? AmountOrTerm.AMOUNT_REDUCE : AmountOrTerm.TERM_REDUCE;

            Spinner sp = (Spinner) list.get(4);
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

    private boolean checkExtraPaymentValues(ArrayList<ArrayList<View>> extraPaymentsViews) {
        for (ArrayList<View> list : extraPaymentsViews
        ) {
            EditText sum = (EditText) list.get(1);
            EditText date = (EditText) list.get(2);
            if (sum.getText().toString().trim().length() == 0) {
                toast = Toast.makeText(context, "Введите сумму частичного погашения", Toast.LENGTH_LONG);
                toast.show();
                return false;
            } else if (date.getText().toString().trim().length() == 0) {
                toast = Toast.makeText(context, "Введите дату частичного погашения", Toast.LENGTH_LONG);
                toast.show();
                return false;
            } else if (LocalDate.parse(date.getText().toString(), DateTimeFormat.forPattern("dd.MM.yyyy")).isBefore(startDate)) {
                toast = Toast.makeText(context, "Дата частичного погашения, раньше даты получения кредита", Toast.LENGTH_LONG);
                toast.show();
                return false;

            }
        }
        return true;
    }

    /**
     Метод выполняется перед началом расчёта и проверяет все ли строки заполнены.
     Если какая-то строка оказывается не заполнена,
     на экран выводится сообщение о пустой строке и метод возвращает false.
     */
    private boolean hasAllDataToCalc() {

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
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    AddExtraPaymentsFragment fragment = new AddExtraPaymentsFragment();
                    transaction.add(R.id.fragment_container_view, fragment);
                    transaction.commit();
                }
            });
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
                        .append(days).append(".")
                        .append(months).append(".")
                        .append(years));
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
    private void updateViews (LinearLayout forUpdate) {
        if (forUpdate.equals(thisLayout)) {}
        else {
            allLayoutLnLo.removeView(thisLayout);
            allLayoutLnLo.addView(forUpdate);
            thisLayout = forUpdate;
        }
    }


}