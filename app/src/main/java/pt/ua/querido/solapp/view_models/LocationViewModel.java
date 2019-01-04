package pt.ua.querido.solapp.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import pt.ua.querido.solapp.database.entity.DayInfo;
import pt.ua.querido.solapp.database.entity.Location;
import pt.ua.querido.solapp.repositories.LocationRepository;

public class LocationViewModel extends ViewModel {

    private LiveData<Location> info;
    private LocationRepository locationRepository;

    @Inject
    public LocationViewModel(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    // ----

    public void init(int id) {
        info = locationRepository.getInfo(id);
    }

    public LiveData<Location> getInfo() {
        return this.info;
    }
}
