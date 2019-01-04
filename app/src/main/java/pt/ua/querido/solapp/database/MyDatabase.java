package pt.ua.querido.solapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import pt.ua.querido.solapp.database.converter.Converter;
import pt.ua.querido.solapp.database.dao.WeatherDao;
import pt.ua.querido.solapp.database.entity.DayInfo;
import pt.ua.querido.solapp.database.entity.Location;

@Database(entities = {DayInfo.class,Location.class}, version = 2)
@TypeConverters(Converter.class)
public abstract class MyDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile MyDatabase INSTANCE;

    // --- DAO ---
    public abstract WeatherDao weatherDao();

}
