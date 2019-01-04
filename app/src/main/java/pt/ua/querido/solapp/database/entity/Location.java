package pt.ua.querido.solapp.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import pt.ua.querido.solapp.database.converter.Local;

@Entity
public class Location {

    @SerializedName("globalIdLocal")
    @Expose
    @PrimaryKey
    @NonNull
    private int globalIdLocal;

    @SerializedName("owner")
    @Expose
    private String owner;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("data")
    @Expose
    private List<Local> data;

    @SerializedName("dataUpdate")
    @Expose
    private String dataUpdate;

    private Date lastRefresh;

    public Location(@NonNull int globalIdLocal, String owner, String country, List<Local> data, String dataUpdate) {
        this.owner = owner;
        this.country = country;
        this.globalIdLocal = globalIdLocal;
        this.data = data;
        this.dataUpdate = dataUpdate;
    }

    @NonNull
    public int getGlobalIdLocal() {
        return globalIdLocal;
    }

    public void setGlobalIdLocal(@NonNull int globalIdLocal) {
        this.globalIdLocal = globalIdLocal;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Local> getData() {
        return data;
    }

    public void setData(List<Local> data) {
        this.data = data;
    }

    public String getDataUpdate() {
        return dataUpdate;
    }

    public void setDataUpdate(String dataUpdate) {
        this.dataUpdate = dataUpdate;
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }
}
