package ca.alexlockhart.a3.rss;

import java.util.ArrayList;

public interface RssCallback {
    void onRssDownloadComplete(ArrayList<WeatherEntry> entries);
}
