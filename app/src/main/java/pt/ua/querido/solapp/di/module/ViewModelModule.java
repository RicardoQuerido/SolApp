package pt.ua.querido.solapp.di.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import pt.ua.querido.solapp.di.key.ViewModelKey;
import pt.ua.querido.solapp.view_models.DayInfoViewModel;
import pt.ua.querido.solapp.view_models.FactoryViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import pt.ua.querido.solapp.view_models.LocationViewModel;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DayInfoViewModel.class)
    abstract ViewModel bindDayInfoViewModel(DayInfoViewModel repoViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LocationViewModel.class)
    abstract ViewModel bindLocationViewModel(LocationViewModel repoViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(FactoryViewModel factory);
}
