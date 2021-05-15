package com.example.currencyconverter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;

public class     MainActivity extends AppCompatActivity {
    public static final String APP_PREFERENCES = "currency_preferences";
    public static final String NOMINAL_PREFERENCE = "currency_nominal_preference";
    public static final String VALUE_PREFERENCE = "currency_value_preference";
    public static final String NAME_PREFERENCE = "currency_name_preference";
    SharedPreferences sharedPreferences;
    TextInputEditText nominal_editText;
    TextInputEditText value_editText;
    TextView price_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        nominal_editText = findViewById(R.id.nominal_editText);
        value_editText = findViewById(R.id.value_editText);
        price_text = findViewById(R.id.price_textView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreferences.contains(NOMINAL_PREFERENCE) && sharedPreferences.contains(VALUE_PREFERENCE)) {
            nominal_editText.setVisibility(TextInputEditText.VISIBLE);
            value_editText.setVisibility(TextInputEditText.VISIBLE);
            price_text.setVisibility(TextView.VISIBLE);
            nominal_editText.setHint(sharedPreferences.getString(NAME_PREFERENCE, ""));
            price_text.setText(
                    sharedPreferences.getInt(NOMINAL_PREFERENCE, 1) + " " +
                            sharedPreferences.getString(NAME_PREFERENCE, "") + " = " +
                            sharedPreferences.getString(VALUE_PREFERENCE, "") + " RUB");

            nominal_editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.i("TEXT CHANGED", s.toString());
                    if (nominal_editText.isFocused()) {
                        value_editText = findViewById(R.id.value_editText);
                        if (s.length() == 0) {
                            value_editText.setText("");
                            nominal_editText.setHint(sharedPreferences.getString(NAME_PREFERENCE, ""));
                            value_editText.setHint("RUB");
                        } else {
                            value_editText.setText(
                                    convertCurrency(sharedPreferences.getInt(NOMINAL_PREFERENCE, 1),
                                            Double.parseDouble(sharedPreferences.getString(VALUE_PREFERENCE, "0")),
                                            Double.parseDouble(s.toString()), false));
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            value_editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.i("TEXT CHANGED", s.toString());
                    if (value_editText.isFocused()) {
                        nominal_editText = findViewById(R.id.nominal_editText);
                        if (s.length() == 0) {
                            value_editText.setHint("RUB");
                            nominal_editText.setText("");
                            nominal_editText.setHint(sharedPreferences.getString(NAME_PREFERENCE, ""));
                        } else {
                            nominal_editText.setText(
                                    convertCurrency(sharedPreferences.getInt(NOMINAL_PREFERENCE, 1),
                                            Double.parseDouble(sharedPreferences.getString(VALUE_PREFERENCE, "0")),
                                            Double.parseDouble(s.toString()), true));
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        } else {
            nominal_editText.setVisibility(TextInputEditText.GONE);
            value_editText.setVisibility(TextInputEditText.GONE);
            price_text.setVisibility(TextView.GONE);
            chooseCurrency(null);
        }
    }

    private String convertCurrency(int nominal, double value, double amount, boolean isReversed) {
        double res;
        if (!isReversed) {
            if (nominal > 1) {
                res = amount * (value / nominal);
            } else res = amount * value;
        } else {
            if (nominal > 1) {
                res = (amount / value) * nominal;
            } else {
                res = amount / value;
            }
        }
        DecimalFormat df = new DecimalFormat("#.####");
        return df.format(res).replace(',', '.');
    }

    public void chooseCurrency(View view) {
        Log.i("INFO", "starting download activity");
        startActivity(new Intent(MainActivity.this, ChooseCurrency.class));
    }
}