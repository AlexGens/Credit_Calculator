package com.samples.ui.textview;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.credit_calculator.R;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // Получаем объекты TextView из ресурсов
        final TextView text3 = (TextView)findViewById(R.id.text3);
        final TextView text4 = (TextView)findViewById(R.id.text4);
        // Устанавливаем текст
        text3.setText("Text from Activity");
        // Загружаем строку текста из ресурсов
        text4.setText(R.string.text_hello);
    }
}