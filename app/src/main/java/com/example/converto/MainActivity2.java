package com.example.converto;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class MainActivity2 extends AppCompatActivity {
    
    TextView convertFromDropdownTextView, convertToDropdownTextView, conversionRateText;
    EditText amountToConvert;
    ArrayList<String> arrayList;
    Dialog fromDialog;
    Button butn;
    Dialog toDialog;
    Button buTTON;
    Button conversionButton;
    String convertFromValue, convertToValue, conversionValue;
    String[] country= { "AED", "AFN","ALL","AMD","ANG", "AOA" ,"ARS","AUD","AWG","AZN","BAM","BBD","BDT","BGN","BHD","BIF","BMD","BND","BOB","BRL","BSD","BTN","BWP","BYN","BZD","CAD","CDF","CHF","CLP","CNY","COP","CRC","CUC","CUP","CVE","CZK","DJF","DKK","DOP","DZD","EGP","ERN","ETB","EUR","FJD","FKP","FOK","GBP","GEL","GGP","GHS","GIP","GMD","GNF","GTQ","GYD","HKD","HNL","HRK","HTG","HUF","IDR","ILS","IMP","INR","IQD","IRR","ISK","JMD","JOD","JPY","KES","KGS","KHR","KID","KMF","KRW","KWD","KYD","KZT","LAK","LBP","LKR","LRD","LSL","MAD","MDL","MGA","MKD","MMK","MNT","MOP","MRU","MUR","MVR","MWK","MXN","MYR","MZN","NAD","NGN","NIO","NOK","NPR","NZD","OMR","PAB","PEN","PGK","PHP","PKR","PLN","PYG","QAR","RON","RSD","RUB","RWF","SAR","SBD","SCR","SDG","SEK","SGD","SHP","SLL","SOS","SRD","STN","SZL","THB","TJS","TMT","TND","TOP","TRY","TTD","TVD","TWD","TZS","UAH","UGX","USD","UYU","UZS","VND","VUV","WST","XAF","XCD","XDR","XOF","XPF","ZAR","ZMW"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        convertFromDropdownTextView = findViewById(R.id.convert_from_dropdown_menu);
        convertToDropdownTextView = findViewById(R.id.convert_to_dropdown_menu);
        conversionButton = findViewById(R.id.conversionButton);
        conversionRateText = findViewById(R.id.conversionRateText);
        amountToConvert = findViewById(R.id.amountToConvertValueEditText);

        butn = findViewById(R.id.Help);





        butn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity2.this,helpActivity.class);
            startActivity(intent);
        });

        buTTON = findViewById(R.id.goBack);
        buTTON.setOnClickListener(view -> startActivity(new Intent(MainActivity2.this,MainActivity.class)));




        arrayList = new ArrayList<>();
        Collections.addAll(arrayList, country);
        convertFromDropdownTextView.setOnClickListener(view -> {
            fromDialog = new Dialog(MainActivity2.this);
            fromDialog.setContentView(R.layout.from_spinner);
            fromDialog.getWindow().setLayout(650,800);
            fromDialog.show();

            EditText editText = fromDialog.findViewById(R.id.edit_text);
            ListView listView = fromDialog.findViewById(R.id.list_view);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity2.this, android.R.layout.simple_list_item_1,arrayList);
            listView.setAdapter(adapter);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            listView.setOnItemClickListener((adapterView, view1, position, id) -> {
                convertFromDropdownTextView.setText(adapter.getItem(position));
                fromDialog.dismiss();
                convertFromValue = adapter.getItem(position);

            });

        });

        convertToDropdownTextView.setOnClickListener(v -> {
            toDialog = new Dialog(MainActivity2.this);
            toDialog.setContentView(R.layout.to_spinner);
            toDialog.getWindow().setLayout(650,800);
            toDialog.show();

            EditText editText = toDialog.findViewById(R.id.edit_text);
            ListView listView = toDialog.findViewById(R.id.list_view);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity2.this, android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(adapter);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            listView.setOnItemClickListener((parent, view, position, id) -> {
                convertToDropdownTextView.setText(adapter.getItem(position));
                toDialog.dismiss();
                convertToValue = adapter.getItem(position);
            });
        });

        conversionButton.setOnClickListener(view -> {
            try {
                Double amountToConvert = Double.valueOf(MainActivity2.this.amountToConvert.getText().toString());
                getConversionRate(convertFromValue, convertToValue, amountToConvert);
            }
            catch(Exception ignored){
            }
        });
    }




    public void getConversionRate(String convertFrom, String convertTo, Double amountToConvert){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://free.currconv.com/api/v7/convert?q="+convertFrom+"_"+convertTo+"&compact=ultra&apiKey=f41942b5dbd23e166a4b";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(response);
                Double conversionRateValue = round(((Double) jsonObject.get(convertFrom + "_" + convertTo)), 2);
                conversionValue = "" + round((conversionRateValue*amountToConvert), 2);
                conversionRateText.setText(conversionValue);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {

        });
        queue.add(stringRequest);
    }
    public static double round(double value, int places){
        if (places<0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();





    }
}