package pt.ua.querido.solapp.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import pt.ua.querido.solapp.api.WeatherWebservice;
import pt.ua.querido.solapp.database.MyDatabase;
import pt.ua.querido.solapp.database.dao.WeatherDao;
import pt.ua.querido.solapp.repositories.DayInfoRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pt.ua.querido.solapp.repositories.LocationRepository;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {

    // --- DATABASE INJECTION ---

    @Provides
    @Singleton
    MyDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application,
                MyDatabase.class, "MyDatabase.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    WeatherDao provideDayInfoDao(MyDatabase database) { return database.weatherDao(); }

    // --- REPOSITORY INJECTION ---

    @Provides
    Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    @Singleton
    LocationRepository provideLocationRepository(WeatherWebservice webservice, WeatherDao weatherDao, Executor executor) {
        return new LocationRepository(webservice, weatherDao, executor);
    }

    @Provides
    @Singleton
    DayInfoRepository provideDayInfoRepository(WeatherWebservice webservice, WeatherDao weatherDao, Executor executor) {
        return new DayInfoRepository(webservice, weatherDao, executor);
    }



    // --- NETWORK INJECTION ---

    private static String BASE_URL = "http://api.ipma.pt/";

    @Provides
    Gson provideGson() { return new GsonBuilder().create(); }

    @Provides
    Retrofit provideRetrofit(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    WeatherWebservice provideApiWebservice(Retrofit restAdapter) {
        return restAdapter.create(WeatherWebservice.class);
    }
}
