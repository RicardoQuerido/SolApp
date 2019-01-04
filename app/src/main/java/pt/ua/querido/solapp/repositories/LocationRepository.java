package pt.ua.querido.solapp.repositories;

import android.arch.lifecycle.LiveData;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import pt.ua.querido.solapp.App;
import pt.ua.querido.solapp.R;
import pt.ua.querido.solapp.api.WeatherWebservice;
import pt.ua.querido.solapp.database.dao.WeatherDao;
import pt.ua.querido.solapp.database.entity.Location;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationRepository {

    private static int FRESH_TIMEOUT_IN_MINUTES = 10;

    private final WeatherWebservice webservice;
    private final WeatherDao weatherDao;
    private final Executor executor;


    @Inject
    public LocationRepository(WeatherWebservice webservice, WeatherDao weatherDao, Executor executor) {
        this.webservice = webservice;
        this.weatherDao = weatherDao;
        this.executor = executor;
    }

    // ---

    public LiveData<Location> getInfo(int id) {
        refreshInfo(id);
        return weatherDao.loadLocation(id);
    }

    // ---

    private void refreshInfo(int id) {
        executor.execute(() -> {
            // Check if user was fetched recently
            boolean dayInfoExists = (weatherDao.hasLocation(id, getMaxRefreshTime(new Date())) != null);
            // If user have to be updated
            if (!dayInfoExists) {
                webservice.getInfoByLocation(Integer.toString(id)).enqueue(new Callback<Location>() {
                    @Override
                    public void onResponse(Call<Location> call, Response<Location> response) {
                        Log.e("TAG", "DATA REFRESHED FROM NETWORK");
                        Toast.makeText(App.context, R.string.refresh_message, Toast.LENGTH_SHORT).show();
                        executor.execute(() -> {
                            Log.d("TAG", call.request().url() + "");
                            Location info = response.body();
                            info.setLastRefresh(new Date());
                            weatherDao.saveLocation(info);
                        });
                    }

                    @Override
                    public void onFailure(Call<Location> call, Throwable t) {
                        Log.d("TAG", "RETROFIT FAILURE.");
                    }
                });
            }
        });
    }

    // ---

    private Date getMaxRefreshTime(Date currentDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.MINUTE, -FRESH_TIMEOUT_IN_MINUTES);
        return cal.getTime();
    }
}
