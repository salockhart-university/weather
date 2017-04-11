package ca.alexlockhart.a3.csv;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FeedsService {

    Context context;
    ArrayList<Feed> feeds;

    public FeedsService(Context context) {
        this.context = context;

        feeds = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("feeds.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                feeds.add(new Feed(row));
            }
            is.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.e("FeedsService", "exception");
        }
    }

    public ArrayList<String> getProvinces() {
        ArrayList<String> provinces = new ArrayList<>();
        for (Feed feed : feeds) {
            if (!provinces.contains(feed.getProvince())) {
                provinces.add(feed.getProvince());
            }
        }
        return provinces;
    }

    public ArrayList<String> getCities(String province) {
        ArrayList<String> cities = new ArrayList<>();
        for (Feed feed : feeds) {
            if (feed.getProvince().equals(province)) {
                cities.add(feed.getCity());
            }
        }
        return cities;
    }

    public String getURL(String province, String city) {
        for (Feed feed : feeds) {
            if (feed.getProvince().equals(province) && feed.getCity().equals(city)) {
                return feed.getUrl();
            }
        }
        return null;
    }
}
