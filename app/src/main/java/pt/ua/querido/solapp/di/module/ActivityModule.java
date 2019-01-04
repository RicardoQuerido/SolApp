package pt.ua.querido.solapp.di.module;

import pt.ua.querido.solapp.activities.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract MainActivity contributeMainActivity();
}
