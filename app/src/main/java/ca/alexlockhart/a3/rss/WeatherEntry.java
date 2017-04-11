package ca.alexlockhart.a3.rss;

public class WeatherEntry {

    private String title;
    private String category;
    private String summary;

    public WeatherEntry() {
    }

    public WeatherEntry(String title, String category, String summary) {
        this.title = title;
        this.category = category;
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "WeatherEntry{" +
                "title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }
}
