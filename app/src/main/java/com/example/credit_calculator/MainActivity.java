package com.example.credit_calculator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;





public class MainActivity extends Activity implements SeekBar.OnSeekBarChangeListener {

    private TextView payoutDurationTxVw;
    private EditText creditSumEdTx;
    private EditText percentEdTx;
    private int creditSum;
    private double percent;
    private TextView result;
    private int payoutDuration;
    private Button calculation;
    private Toast toast;
    private View rectangleView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final   Context context = getApplicationContext();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        SeekBar mySeekbar = (SeekBar) findViewById(R.id.seekBar);         // Создаём ползунок количества месяцев
        mySeekbar.setOnSeekBarChangeListener(this);
        creditSumEdTx = (EditText) findViewById(R.id.creditSumId);        // Окно ввода суммы кредита
        CreditType creditType = CreditType.ANNUITY;                       // Вид платежа
        calculation = (Button) findViewById(R.id.calculationXML);         // Кнопка "расчитать"
        payoutDurationTxVw = (TextView) findViewById(R.id.SeekBarNumber); // Значение количества месяцев (число)
        percentEdTx = (EditText) findViewById(R.id.procentXML);           // Окно ввода процентов по кредиту
        payoutDurationTxVw.setText("3");
        result = (TextView) findViewById(R.id.resultXML);                 // Вывод суммы платежа
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
               payoutDuration = Integer.parseInt(payoutDurationTxVw.getText().toString());
               try {
                   percent = Double.parseDouble(percentEdTx.getText().toString()) / 100;
               } catch (Exception e) {
                   toast = Toast.makeText(context, "Введите процентную ставку",Toast.LENGTH_LONG );
                   toast.show();
                   return;
               }
               CalcAnnuitet calculator = new CalcAnnuitet(creditSum, percent, payoutDuration);
           result.setText(String.valueOf(calculator.getResultBySumAndProcent()));




           }
       });




    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        payoutDurationTxVw.setText(String.valueOf(seekBar.getProgress() + 3));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        payoutDurationTxVw.setText(String.valueOf(seekBar.getProgress() + 3));
    }
}