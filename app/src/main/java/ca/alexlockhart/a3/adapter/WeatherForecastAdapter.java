package ca.alexlockhart.a3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ca.alexlockhart.a3.R;
import ca.alexlockhart.a3.rss.WeatherEntry;
import ca.alexlockhart.a3.util.ForecastService;

public class WeatherForecastAdapter extends BaseAdapter {

    LayoutInflater inflater;
    private Context context;
    private List<WeatherEntry> entries;

    private ForecastService forecastService;

    public WeatherForecastAdapter(Context context, List<WeatherEntry> entries) {
        this.context = context;
        this.entries = entries;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        forecastService = new ForecastService(context);
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public Object getItem(int position) {
        return entries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.weather_entry, null);
        }

        String forecast = entries.get(position).getTitle();
        String conditions = forecastService.getConditionsFromForecast(forecast);

        TextView title = (TextView) convertView.findViewById(R.id.weather_entry_title);
        title.setText(forecast);

        ImageView icon = (ImageView) convertView.findViewById(R.id.weather_entry_icon);
        icon.setImageResource(forecastService.getIconIdFromConditions(forecastService.isNightForecast(forecast), conditions));

        return convertView;
    }
}
