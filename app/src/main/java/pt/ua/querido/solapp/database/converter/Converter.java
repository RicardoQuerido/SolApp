package pt.ua.querido.solapp.database.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Converter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static List<Region> stringToRegion(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Region>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String regionToString(List<Region> regions) {
        return gson.toJson(regions);
    }



    @TypeConverter
    public static List<Local> stringToLocal(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Local>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String localToString(List<Local> locals) {
        return gson.toJson(locals);
    }



    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
