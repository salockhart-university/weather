package ca.alexlockhart.a3.rss;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RssDownloader extends AsyncTask<String, Void, InputStream> {

    RssCallback callback;

    public RssDownloader(RssCallback callback) {
        this.callback = callback;
    }

    @Override
    protected InputStream doInBackground(String... urls) {
        return GET(urls[0]);
    }

    @Override
    protected void onPostExecute(InputStream stream) {
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(stream, null);

            ArrayList<WeatherEntry> entries = parseXML(parser);
            stream.close();

            callback.onRssDownloadComplete(entries);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("RssDownloader", "exception");
        }
    }

    InputStream GET(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(10000 /* milliseconds */);
            connection.setConnectTimeout(15000 /* milliseconds */);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            return connection.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("RssDownloader", "exception");
            return null;
        }
    }

    ArrayList<WeatherEntry> parseXML(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<WeatherEntry> entries = null;
        int eventType = parser.getEventType();
        WeatherEntry WeatherEntry = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    entries = new ArrayList<WeatherEntry>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals("entry")) {
                        WeatherEntry = new WeatherEntry();
                    } else if (WeatherEntry != null) {
                        switch (name) {
                            case "title":
                                WeatherEntry.setTitle(parser.nextText());
                                break;
                            case "category":
                                WeatherEntry.setCategory(parser.getAttributeValue(null, "term"));
                                break;
                            case "summary":
                                WeatherEntry.setSummary(parser.nextText());
                                break;
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equals("entry") && WeatherEntry != null) {
                        entries.add(WeatherEntry);
                    }
                    break;
            }
            eventType = parser.next();
        }

        return entries;
    }
}