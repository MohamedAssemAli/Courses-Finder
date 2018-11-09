package com.example.haifaalmeshari.coursefinder.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.haifaalmeshari.coursefinder.App.AppConfig;
import com.example.haifaalmeshari.coursefinder.R;

import java.util.ArrayList;
import java.util.List;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Spinner spinner = findViewById(R.id.spinner);
        List<String> spinnerList = new ArrayList<>();
        spinnerList.add("Select City");
        spinnerList.add("Riyadh");
        spinnerList.add("Dammam");
        spinnerList.add("Jeddah");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final Intent intent = new Intent(MainPage.this, CityActivity.class);
                switch (i) {
                    case 1:
                        intent.putExtra(AppConfig.INTENT_CITY_KEY, AppConfig.RIYADH);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.putExtra(AppConfig.INTENT_CITY_KEY, AppConfig.DAMMAM);
                        startActivity(intent);
                        break;
                    case 3:
                        intent.putExtra(AppConfig.INTENT_CITY_KEY, AppConfig.JEDDAH);
                        startActivity(intent);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void btn_next(View v) {
        Intent intent = new Intent(MainPage.this, CoursesList.class);
        startActivity(intent);
    }
}
