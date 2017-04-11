package ca.alexlockhart.a3.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ca.alexlockhart.a3.R;
import ca.alexlockhart.a3.adapter.WeatherForecastAdapter;
import ca.alexlockhart.a3.csv.FeedsService;
import ca.alexlockhart.a3.rss.RssCallback;
import ca.alexlockhart.a3.rss.RssDownloader;
import ca.alexlockhart.a3.rss.WeatherEntry;
import ca.alexlockhart.a3.sql.Location;
import ca.alexlockhart.a3.sql.LocationService;
import ca.alexlockhart.a3.util.ForecastService;

import static android.R.color.holo_green_dark;
import static android.R.color.holo_green_light;
import static android.R.color.holo_red_light;

public class MainActivity extends AppCompatActivity implements RssCallback {

    LocationService locationService;
    FeedsService feedsService;
    ForecastService forecastService;

    TextView warningsView;
    ImageView currentIcon;
    TextView currentTitleView;
    TextView currentSummaryView;
    ListView forecastView;

    WeatherEntry warnings;
    WeatherEntry current;
    ArrayList<WeatherEntry> forecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationService = new LocationService(this);
        feedsService = new FeedsService(this);
        forecastService = new ForecastService(this);

        warningsView = (TextView) findViewById(R.id.main_warnings);
        currentIcon = (ImageView) findViewById(R.id.main_current_icon);
        currentTitleView = (TextView) findViewById(R.id.main_current_title);
        currentSummaryView = (TextView) findViewById(R.id.main_current_summary);
        forecastView = (ListView) findViewById(R.id.main_forecast);

        Location location = locationService.getLocation();

        if (location == null) {
            toLocationActivity();
            return;
        }

        setTitle(location.getCity() + ", " + location.getProvince());

        String url = feedsService.getURL(location.getProvince(), location.getCity());

        new RssDownloader(this).execute(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                toLocationActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRssDownloadComplete(ArrayList<WeatherEntry> entries) {
        for (WeatherEntry entry : entries) {
            Log.d("entry_title", entry.getTitle());
            Log.d("entry_category", entry.getCategory());
            Log.d("entry_summary", entry.getSummary());
        }

        warnings = entries.get(0);
        current = entries.get(1);
        forecast = new ArrayList<>(entries.subList(2, entries.size()));

        warningsView.setText(warnings.getTitle());
        if (warnings.getTitle().startsWith("No watches")) {
            warningsView.setBackgroundColor(getResources().getColor(holo_green_light));
        } else {
            warningsView.setBackgroundColor(getResources().getColor(holo_red_light));
        }

        currentTitleView.setText(current.getTitle());
        currentSummaryView.setText(Html.fromHtml(current.getSummary()));

        String currentForecast = forecastService.getConditionsFromCurrentSummary(current.getSummary());
        String currentConditions = forecastService.getConditionsFromForecast(currentForecast);
        currentIcon.setImageResource(forecastService.getIconIdFromConditions(false, currentConditions));

        forecastView.setAdapter(new WeatherForecastAdapter(this, forecast));
        forecastView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toDetailActivity(view, forecast.get(position));
            }
        });
    }

    public void toLocationActivity() {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
    }

    public void viewWarnings(View view) {
        toDetailActivity(view, warnings);
    }

    public void toDetailActivity(View view, WeatherEntry weatherEntry) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("weather_title", weatherEntry.getTitle());
        intent.putExtra("weather_category", weatherEntry.getCategory());
        intent.putExtra("weather_summary", weatherEntry.getSummary());
        startActivity(intent);
    }
}
