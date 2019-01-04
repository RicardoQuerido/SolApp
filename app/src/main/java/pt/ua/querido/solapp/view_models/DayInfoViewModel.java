package pt.ua.querido.solapp.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import pt.ua.querido.solapp.database.entity.DayInfo;
import pt.ua.querido.solapp.repositories.DayInfoRepository;

public class DayInfoViewModel extends ViewModel {

    private LiveData<DayInfo> info;
    private DayInfoRepository dayInfoRepository;

    @Inject
    public DayInfoViewModel(DayInfoRepository dayInfoRepository) {
        this.dayInfoRepository = dayInfoRepository;
    }

    // ----

    public void init(String day_number, FragmentActivity activity) {
        info = dayInfoRepository.getInfo(day_number, activity);
    }

    public LiveData<DayInfo> getInfo() {
        return this.info;
    }

}
