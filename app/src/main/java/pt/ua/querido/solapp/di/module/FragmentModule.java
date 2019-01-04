package pt.ua.querido.solapp.di.module;

import pt.ua.querido.solapp.fragments.DayInfoFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pt.ua.querido.solapp.fragments.HomePageFragment;
import pt.ua.querido.solapp.fragments.LocationFragment;

@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract DayInfoFragment contributeDayInfoFragment();

    @ContributesAndroidInjector
    abstract HomePageFragment contributeHomePageFragment();

    @ContributesAndroidInjector
    abstract LocationFragment contributeLocationFragment();
}
