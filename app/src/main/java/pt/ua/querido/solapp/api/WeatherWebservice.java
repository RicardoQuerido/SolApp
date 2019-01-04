package pt.ua.querido.solapp.api;

import pt.ua.querido.solapp.database.entity.DayInfo;
import pt.ua.querido.solapp.database.entity.Location;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WeatherWebservice {
    @GET("open-data/forecast/meteorology/cities/daily/hp-daily-forecast-day{day_number}")
    Call<DayInfo> getInfo(@Path("day_number") String day_number);

    @GET("open-data/forecast/meteorology/cities/daily/{location_id}")
    Call<Location> getInfoByLocation(@Path("location_id") String location_id);
}
