package ca.alexlockhart.a3.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import ca.alexlockhart.a3.R;
import ca.alexlockhart.a3.csv.FeedsService;
import ca.alexlockhart.a3.sql.Location;
import ca.alexlockhart.a3.sql.LocationService;

public class LocationActivity extends AppCompatActivity {

    Spinner spinnerProvince;
    Spinner spinnerCity;

    String selectedProvince;
    String selectedCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        final FeedsService feedsService = new FeedsService(this);

        spinnerProvince = (Spinner) findViewById(R.id.spinner_province);
        spinnerCity = (Spinner) findViewById(R.id.spinner_city);

        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedProvince = ((TextView) view).getText().toString();
                setCities(feedsService.getCities(selectedProvince));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = ((TextView) view).getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setProvinces(feedsService.getProvinces());
    }

    void setProvinces(ArrayList<String> provinces) {
        setSpinner(spinnerProvince, provinces);
    }

    void setCities(ArrayList<String> cities) {
        setSpinner(spinnerCity, cities);
    }

    void setSpinner(Spinner spinner, ArrayList<String> values) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, values.toArray(new String[0]));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    public void onClick(View view) {
        LocationService service = new LocationService(this);
        service.setLocation(new Location(selectedProvince, selectedCity));
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
