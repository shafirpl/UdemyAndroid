package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void convertCurrency(View view){
        EditText editText = (EditText)findViewById(R.id.editText);

        String amountEntered = editText.getText().toString();
        double amountInCad = Double.parseDouble(amountEntered);
        String amountInUSD = String.format("%.2f",amountInCad / 1.3) ;

        Toast.makeText(this,"$"+amountInUSD, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
