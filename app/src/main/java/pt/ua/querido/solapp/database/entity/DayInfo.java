package pt.ua.querido.solapp.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import pt.ua.querido.solapp.database.converter.Region;

@Entity
public class DayInfo {

    @SerializedName("owner")
    @Expose
    private String owner;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("forecastDate")
    @Expose
    private String forecastDate;

    @SerializedName("data")
    @Expose
    private List<Region> data;

    @SerializedName("dataUpdate")
    @Expose
    private String dataUpdate;

    @PrimaryKey
    @NonNull
    private String day;

    private int currentLocation;
    private Date lastRefresh;

    // --- CONSTRUCTORS ---

    public DayInfo(String owner, String country, String forecastDate, List<Region> data, String dataUpdate, Date lastRefresh, @NonNull String day, int currentLocation) {
        this.owner = owner;
        this.country = country;
        this.forecastDate = forecastDate;
        this.data = data;
        this.dataUpdate = dataUpdate;
        this.lastRefresh = lastRefresh;
        this.day = day;
        this.currentLocation = currentLocation;
    }

    // --- GETTER ---

    public String getOwner() { return owner; }
    public String getCountry() { return country; }
    public List<Region> getData() {return data; }
    public Date getLastRefresh() { return lastRefresh; }
    public String getForecastDate() { return forecastDate; }
    public String getDataUpdate() { return dataUpdate; }
    public String getDay(){ return day; }
    public int getCurrentLocation(){ return currentLocation; }

    // --- SETTER ---

    public void setOwner(String owner) { this.owner = owner; }
    public void setCountry(String country) { this.country = country; }
    public void setData(List<Region> data) {this.data = data; }
    public void setLastRefresh(Date lastRefresh) { this.lastRefresh = lastRefresh; }
    public void setForecastDate(String forecastDate) { this.forecastDate = forecastDate; }
    public void setDataUpdate(String dataUpdate) { this.dataUpdate = dataUpdate; }
    public void setDay(String day) { this.day = day;}
    public void setCurrentLocation(int currentLocation) { this.currentLocation = currentLocation;}
}
