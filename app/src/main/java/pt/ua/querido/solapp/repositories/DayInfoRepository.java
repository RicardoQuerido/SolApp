package pt.ua.querido.solapp.repositories;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import pt.ua.querido.solapp.App;
import pt.ua.querido.solapp.R;
import pt.ua.querido.solapp.api.WeatherWebservice;
import pt.ua.querido.solapp.database.converter.Region;
import pt.ua.querido.solapp.database.dao.WeatherDao;
import pt.ua.querido.solapp.database.entity.DayInfo;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class DayInfoRepository {

    private static int FRESH_TIMEOUT_IN_MINUTES = 10;

    private final WeatherWebservice webservice;
    private final WeatherDao weatherDao;
    private final Executor executor;
    private FusedLocationProviderClient mFusedLocationClient;
    private FragmentActivity activity;
    private String CURRENT_LOCATION;


    @Inject
    public DayInfoRepository(WeatherWebservice webservice, WeatherDao weatherDao, Executor executor) {
        this.webservice = webservice;
        this.weatherDao = weatherDao;
        this.executor = executor;
        this.mFusedLocationClient = null;
        this.activity = null;
    }

    private void setActivity(FragmentActivity activity){
        this.activity = activity;
        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    // ---

    public LiveData<DayInfo> getInfo(String day_number, FragmentActivity activity) {
        setActivity(activity);
        refreshInfo(day_number);
        return weatherDao.loadDayInfo(day_number);
    }

    // ---

    private void refreshInfo(String day_number) {
        executor.execute(() -> {
            // Check if user was fetched recently
            boolean dayInfoExists = (weatherDao.hasDayInfo(day_number, getMaxRefreshTime(new Date())) != null);
            checkCurrentLocation();
            // If user have to be updated
            if (!dayInfoExists) {
                webservice.getInfo(day_number).enqueue(new Callback<DayInfo>() {
                    @Override
                    public void onResponse(Call<DayInfo> call, Response<DayInfo> response) {
                        Log.d("TAG", "DATA REFRESHED FROM NETWORK");
                        Toast.makeText(App.context, R.string.refresh_message, Toast.LENGTH_SHORT).show();
                        executor.execute(() -> {
                            DayInfo info = response.body();
                            info.setLastRefresh(new Date());
                            info.setDay(day_number);
                            info.setCurrentLocation(locationToID(info.getData()));
                            weatherDao.saveDayInfo(info);
                        });
                    }

                    @Override
                    public void onFailure(Call<DayInfo> call, Throwable t) {
                        Log.d("TAG", "RETROFIT FAILURE.");
                    }
                });
            }
        });
    }

    private int locationToID(List<Region> data){
        Geocoder geocoder = new Geocoder(activity,Locale.getDefault());
        double lt, lg;
        Integer id;
        for(int i = 0; i < data.size(); i++) {
            lt = Double.valueOf(data.get(i).getLatitude());
            lg = Double.valueOf(data.get(i).getLongitude());
            id = data.get(i).getGlobalIdLocal();
            try {
                List<Address> locations = geocoder.getFromLocation(lt, lg, 1);
                if (!locations.isEmpty()) {
                    String city_name = locations.get(0).getAdminArea();
                    if (city_name != null && city_name.equals(CURRENT_LOCATION)) {
                        return data.get(i).getGlobalIdLocal();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    private void checkCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    FRESH_TIMEOUT_IN_MINUTES = 1;
                    try {
                        Geocoder geocoder = new Geocoder(activity.getSupportFragmentManager().getFragments().get(0).getContext(), Locale.getDefault());
                        List<Address> locations = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (!locations.isEmpty()) {
                            CURRENT_LOCATION = locations.get(0).getAdminArea();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("TAG","location is null");
                    FRESH_TIMEOUT_IN_MINUTES = 0;
                    CURRENT_LOCATION = "NOT_FOUND";
                }
            }
        });
        mFusedLocationClient.getLastLocation().addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","fused location fail");
                CURRENT_LOCATION = "NOT_FOUND";
                FRESH_TIMEOUT_IN_MINUTES = 0;
            }
        });
    }

    // ---

    private Date getMaxRefreshTime(Date currentDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.MINUTE, -FRESH_TIMEOUT_IN_MINUTES);
        return cal.getTime();
    }
}
