package info.alarmap.us.alarmap._models;

/**
 * Created by julianperez on 6/14/17.
 */

public class Post {
    public String id;
    public Double lat;
    public Double lon;
    public String tipeId;

    public Post() {

    }

    public Post(String id,Double lat,Double lon, String tipeId) {
        this.id   = id;
        this.lat  = lat;
        this.lon  = lon;
        this.tipeId = tipeId;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getTipeId() {return tipeId; }

    public void setTipeId(String tipeId) { this.tipeId = tipeId; }
}
