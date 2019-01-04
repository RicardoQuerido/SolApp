package pt.ua.querido.solapp.di.component;

import android.app.Application;

import pt.ua.querido.solapp.App;
import pt.ua.querido.solapp.di.module.ActivityModule;
import pt.ua.querido.solapp.di.module.AppModule;
import pt.ua.querido.solapp.di.module.FragmentModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules={AndroidSupportInjectionModule.class, ActivityModule.class, FragmentModule.class, AppModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

    void inject(App app);
}
