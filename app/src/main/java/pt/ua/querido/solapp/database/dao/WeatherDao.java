package pt.ua.querido.solapp.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import pt.ua.querido.solapp.database.entity.DayInfo;
import pt.ua.querido.solapp.database.entity.Location;

import java.util.Date;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface WeatherDao {

    @Insert(onConflict = REPLACE)
    void saveDayInfo(DayInfo dayinfo);

    @Insert(onConflict = REPLACE)
    void saveLocation(Location location);

    @Query("SELECT * FROM dayinfo WHERE day = :day")
    LiveData<DayInfo> loadDayInfo(String day);

    @Query("SELECT * FROM location WHERE globalIdLocal = :id")
    LiveData<Location> loadLocation(int id);

    @Query("SELECT * FROM dayinfo WHERE day = :day AND lastRefresh > :lastRefreshMax LIMIT 1")
    DayInfo hasDayInfo(String day, Date lastRefreshMax);

    @Query("SELECT * FROM location WHERE globalIdLocal = :id AND lastRefresh > :lastRefreshMax LIMIT 1")
    Location hasLocation(int id, Date lastRefreshMax);

}