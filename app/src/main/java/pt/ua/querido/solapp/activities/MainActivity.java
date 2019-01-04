package pt.ua.querido.solapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import pt.ua.querido.solapp.R;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import pt.ua.querido.solapp.fragments.DayInfoFragment;
import pt.ua.querido.solapp.fragments.HomePageFragment;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    private int currentScreen;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.currentScreen = 0;

        this.configureDagger();
        this.showFragment(savedInstanceState);
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    // ---

    private void showFragment(Bundle savedInstanceState){
        if (savedInstanceState == null) {

            HomePageFragment fragment = new HomePageFragment();

            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, null)
                    .commit();
        }
    }

    private void configureDagger(){
        AndroidInjection.inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = new Bundle();
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_home:
                HomePageFragment hfragment = new HomePageFragment();

                hfragment.setArguments(bundle);


                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, hfragment, null)
                        .commit();
                return true;
            case R.id.menu_map:
                DayInfoFragment fragment = new DayInfoFragment();

                fragment.setArguments(bundle);


                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment, null)
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        switch(currentScreen){
            case 0:
                finish();
                System.exit(0);
            case 1:
                HomePageFragment hfragment = new HomePageFragment();

                hfragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, hfragment, null)
                        .commit();
                break;
            case 2:
                DayInfoFragment fragment = new DayInfoFragment();

                fragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment, null)
                        .commit();
                break;
            default:
                finish();
                System.exit(-1);
                break;
        }
    }

    public void setCurrentScreen(int screen){
        this.currentScreen = screen;
    }
}
