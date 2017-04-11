package ca.alexlockhart.a3.csv;

public class Feed {
    String url;
    String province;
    String city;

    public Feed(String[] row) {
        this.url = row[0].trim();
        this.province = row[2].trim();
        this.city = row[1].trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
