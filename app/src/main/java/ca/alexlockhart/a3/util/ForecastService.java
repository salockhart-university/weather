package ca.alexlockhart.a3.util;

import android.content.Context;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.alexlockhart.a3.R;

public class ForecastService {

    private static final String CONDITIONS_FROM_SUMMARY = "<b>Condition:<\\/b>\\s+(.*?)\\s+<br\\/>";
    private static final String IS_NIGHT_FORECAST = "((^Sunday)|(^Monday)|(^Tuesday)|(^Wednesday)|(^Thursday)|(^Friday)|(^Saturday))\\snight.*";
    private Context context;

    public ForecastService(Context context) {
        this.context = context;
    }

    public String getConditionsFromCurrentSummary(String summary) {
        Pattern pattern = Pattern.compile(CONDITIONS_FROM_SUMMARY);
        Matcher matcher = pattern.matcher(summary);
        if (matcher.find() && matcher.groupCount() >= 1) {
            return matcher.group(1);
        }
        return "";
    }

    public boolean isNightForecast(String forecast) {
        Pattern pattern = Pattern.compile(IS_NIGHT_FORECAST);
        Matcher matcher = pattern.matcher(forecast);
        return matcher.find();
    }

    public String getConditionsFromForecast(String forecast) {
        forecast = forecast.toLowerCase();
        if (forecast.contains("fog")) {
            return "fog";
        }

        if (forecast.contains("lightning") || forecast.contains("thunder")) {
            return "lightning";
        }

        if (forecast.contains("snow") || forecast.contains("flurr")) {
            return "snow";
        }

        if (forecast.contains("rain") || forecast.contains("shower")) {
            return "rain";
        }

        if (forecast.contains("wind")) {
            return "wind";
        }

        if (forecast.contains("cloud")) {
            return "cloud";
        }

        return "";
    }

    /*
     * Uses the climacons icon pack.
     * Whitcroft, A. (n.d.). Climacons. Retrieved March 11, 2017, from http://adamwhitcroft.com/climacons/
     */
    public int getIconIdFromConditions(boolean isNight, String conditions) {
        switch (conditions) {
            case "fog":
                return isNight ? R.drawable.climacons_cloud_fog_moon : R.drawable.climacons_cloud_fog_sun;
            case "lightning":
                return isNight ? R.drawable.climacons_cloud_lightning_moon : R.drawable.climacons_cloud_lightning_sun;
            case "rain":
                return isNight ? R.drawable.climacons_cloud_rain_moon : R.drawable.climacons_cloud_rain_sun;
            case "snow":
                return isNight ? R.drawable.climacons_cloud_snow_moon : R.drawable.climacons_cloud_snow_sun;
            case "wind":
                return isNight ? R.drawable.climacons_cloud_wind_moon : R.drawable.climacons_cloud_wind_sun;
            case "cloud":
                return isNight ? R.drawable.climacons_cloud_moon : R.drawable.climacons_cloud_sun;
            default:
                return isNight ? R.drawable.climacons_moon : R.drawable.climacons_sun;
        }
    }

    public String getDayFromForecast(String forecast) {
        String[] days = new String[] {
                "Sunday",
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday"
        };

        for (String day : days) {
            if (forecast.startsWith(day)) {
                return day;
            }
        }

        return "";
    }
}
