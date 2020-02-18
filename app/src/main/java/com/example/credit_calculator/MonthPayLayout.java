package com.example.credit_calculator;

import android.content.Context;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.view.ContextThemeWrapper;

public class MonthPayLayout extends LinearLayout {
    private  static  Context context;
    private  static   MonthPayLayout basicLayout;
    private  static  LinearLayout enterCredSumLO;
    private  static  ImageView enterCredSumImVw;
    private  static  EditText enterCredSumEdTx;
    private  static  LinearLayout enterPercentLO;
    private  static  ImageView enterPercentImVw;
    private  static  EditText enterPercentEdTx;
    private  static  LinearLayout enterPayoutDurLO;
    private  static  ImageView enterPayoutDurImVw;
    private  static  EditText enterPayoutDurEdTx;
    private  static  Spinner enterPayoutDurSpnr;
    private  static  LinearLayout enterStartDateLO;
    private  static  ImageView enterStartDateImVw;
    private  static  EditText enterStartDateEdTx;
    private  static  LinearLayout enterMonthPayLO;
    private  static  ImageView enterMonthPayImVw;
    private  static  EditText enterMonthPayEdTx;
    private  static  RadioGroup enterCreditTypeRdGp;
    private  static  RadioButton enterCreditTypeAnnuitRdBn;
    private  static  RadioButton enterCreditTypeDifferenceRdBn;

    static {

    }

    public static   MonthPayLayout getInstance(Context context) {
        if (basicLayout == null) {
            MonthPayLayout.basicLayout = new MonthPayLayout(context);
            setViews();
            buildBasicLayout();
        }
        return basicLayout;
    }

    private static void buildBasicLayout() {
        basicLayout.addView(enterCredSumLO);
        enterCredSumLO.addView(enterCredSumImVw);
        enterCredSumLO.addView(enterCredSumEdTx);
        basicLayout.addView(enterPercentLO);
        enterPercentLO.addView(enterPercentImVw);
        enterPercentLO.addView(enterPercentEdTx);
        basicLayout.addView(enterPayoutDurLO);
        enterPayoutDurLO.addView(enterPayoutDurImVw);
        enterPayoutDurLO.addView(enterPayoutDurEdTx);
        enterPayoutDurLO.addView(enterPayoutDurSpnr);
        basicLayout.addView(enterStartDateLO);
        enterStartDateLO.addView(enterStartDateImVw);
        enterStartDateLO.addView(enterStartDateEdTx);
        basicLayout.addView(enterCreditTypeRdGp);
        enterCreditTypeRdGp.addView(enterCreditTypeAnnuitRdBn);
        enterCreditTypeRdGp.addView(enterCreditTypeDifferenceRdBn);
    }


    private MonthPayLayout(Context context) {
        super(context);
        MonthPayLayout.context = context;
    }

    private static void setViews() {
        LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        basicLayout.setOrientation(LinearLayout.VERTICAL);
        basicLayout.setLayoutParams(params);
        basicLayout.setId(R.id.payment_data_layout_id);


        //Сумма кредита
        enterCredSumLO = new LinearLayout(context, null, 0, R.style.EditText_Layout);
        enterCredSumLO.setId(R.id.credit_sum_layout_id);
        enterCredSumImVw = new ImageView(context, null,0, R.style.ImageView_EnterCredSum);
        enterCredSumImVw.setId(R.id.credit_sum_image_id);
        enterCredSumEdTx = new EditText(context, null,0, R.style.EditText_EnterCreditSum);
        enterCredSumEdTx.setId(R.id.credit_sum_id);
        //Проценты
        enterPercentLO = new LinearLayout(context, null,0, R.style.EditText_Layout);
        enterPercentLO.setId(R.id.percent_layout_id);
        enterPercentImVw =  new ImageView(context, null,0, R.style.ImageView_EnterPercent);
        enterPercentImVw.setId(R.id.percent_image_id);
        enterPercentEdTx = new EditText(context, null,0, R.style.EditText_EnterPercent);
        enterPercentEdTx.setId(R.id.percent_id);
        //Срок выплат
        enterPayoutDurLO = new LinearLayout(context, null, 0,R.style.EditText_Layout);
        enterPayoutDurLO.setId(R.id.payout_duration_layout_id);
        enterPayoutDurImVw =  new ImageView(context, null,0, R.style.ImageView_EnterPayoutDur);
        enterPayoutDurImVw.setId(R.id.payout_duration_image_id);
        enterPayoutDurEdTx = new EditText(context, null,0, R.style.EditText_EnterPayoutDur);
        enterPayoutDurEdTx.setId(R.id.payout_duration_id);
        enterPayoutDurSpnr = new Spinner(context, null, R.style.spinner_payout_dur);
        enterPayoutDurSpnr.setId(R.id.spinner_payout_dur_id);
        //Дата начала выплат
        enterStartDateLO = new LinearLayout(context, null,0, R.style.EditText_Layout);
        enterStartDateLO.setId(R.id.date_of_start_layout_id);
        enterStartDateImVw =  new ImageView(context, null, 0,  R.style.EditText_EnterStartDate);
        enterStartDateImVw.setId(R.id.date_of_start_image_id);
        enterStartDateEdTx = new EditText(context, null,0, R.style.EditText_EnterStartDate);
        enterStartDateEdTx.setId(R.id.date_of_start_id);
        //Ежемесячный платёж
        enterMonthPayLO = new LinearLayout(context, null,0, R.style.EditText_Layout);
        enterMonthPayLO.setId(R.id.month_pay_layout_id);
        enterMonthPayImVw =  new ImageView(context, null,0, R.style.ImageView_EnterMonthPay);
        enterMonthPayImVw.setId(R.id.month_pay_image_id);
        enterMonthPayEdTx = new EditText(context, null,0, R.style.EditText_EnterMonthPay);
        enterMonthPayEdTx.setId(R.id.month_pay_id);
        //Тип кредита
        LayoutParams creditTypeParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        enterCreditTypeRdGp = new RadioGroup(context);
        enterCreditTypeRdGp.setLayoutParams(creditTypeParams);
        enterCreditTypeRdGp.setPadding(16,16,16,16);
        enterCreditTypeRdGp.setId(R.id.credit_type_id);
        enterCreditTypeAnnuitRdBn = new RadioButton(context, null, R.style.CreditType_annuitet);
        enterCreditTypeAnnuitRdBn.setId(R.id.credit_type_annuitet_id);
        enterCreditTypeDifferenceRdBn = new RadioButton(context, null, R.style.CreditType_difference);
        enterCreditTypeDifferenceRdBn.setId(R.id.credit_type_difference_id);
    }


}
