package ca.alexlockhart.a3.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import ca.alexlockhart.a3.R;
import ca.alexlockhart.a3.rss.WeatherEntry;
import ca.alexlockhart.a3.sql.Location;
import ca.alexlockhart.a3.util.ForecastService;

public class DetailActivity extends AppCompatActivity {

    ForecastService forecastService;

    ImageView icon;
    TextView title;
    TextView summary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        forecastService = new ForecastService(this);

        WeatherEntry weatherEntry = new WeatherEntry();
        weatherEntry.setTitle(getIntent().getExtras().getString("weather_title"));
        weatherEntry.setCategory(getIntent().getExtras().getString("weather_category"));
        weatherEntry.setSummary(getIntent().getExtras().getString("weather_summary"));

        icon = (ImageView) findViewById(R.id.detail_icon);
        title = (TextView) findViewById(R.id.detail_title);
        summary = (TextView) findViewById(R.id.detail_summary);

        title.setText(weatherEntry.getTitle());
        summary.setText(weatherEntry.getSummary());

        String conditions = forecastService.getConditionsFromForecast(weatherEntry.getTitle());
        boolean isNight = forecastService.isNightForecast(weatherEntry.getTitle());

        icon.setImageResource(forecastService.getIconIdFromConditions(isNight, conditions));

        String day = forecastService.getDayFromForecast(weatherEntry.getTitle());

        if (day.equals("")) {
            setTitle("Warnings");
            icon.setVisibility(View.INVISIBLE);
        } else {
            icon.setVisibility(View.VISIBLE);
            if (isNight) {
                setTitle(day + " Night");
            } else {
                setTitle(day);
            }
        }
    }
}
