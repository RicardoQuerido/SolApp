package pt.ua.querido.solapp.fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import pt.ua.querido.solapp.R;
import pt.ua.querido.solapp.activities.MainActivity;
import pt.ua.querido.solapp.database.converter.Region;
import pt.ua.querido.solapp.database.entity.DayInfo;
import pt.ua.querido.solapp.view_models.DayInfoViewModel;

public class HomePageFragment extends Fragment{

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DayInfoViewModel viewModel;


    // FOR DESIGN
    @BindView(R.id.location_name)
    TextView location_name;

    @BindView(R.id.max_temperature)
    TextView max_temperature;

    @BindView(R.id.min_temperature)
    TextView min_temperature;

    @BindView(R.id.precipitation)
    TextView precipitation;

    @BindView(R.id.wind_direction)
    TextView wind_direction;

    @BindView(R.id.map_btn)
    Button map_btn;

    @BindView(R.id.rising_sun)
    ImageView rising_sun;

    Animation up_animation;

    public HomePageFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).setCurrentScreen(0);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupThings();
        this.configureDagger();
        this.configureViewModel();

    }

    private void setupThings() {
        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DayInfoFragment fragment = new DayInfoFragment();

                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment, null)
                        .commit();
            }
        });
        up_animation = AnimationUtils.loadAnimation(getActivity(),R.anim.up_animation);
        rising_sun.setAnimation(up_animation);

    }

    // -----------------
    // CONFIGURATION
    // -----------------

    private void configureDagger(){
        AndroidSupportInjection.inject(this);
    }

    private void configureViewModel(){
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DayInfoViewModel.class);
        viewModel.init("0",getActivity());
        viewModel.getInfo().observe(this, info -> updateUI(info));
    }

    // -----------------
    // UPDATE UI
    // -----------------

    private void updateUI(@Nullable DayInfo info){
        if (info != null){
            List<Region> data = info.getData();
            Geocoder geocoder = new Geocoder(getContext(),Locale.getDefault());
            double lt, lg;
            Integer id;
            for(int i = 0; i < data.size(); i++){
                lt = Double.valueOf(data.get(i).getLatitude());
                lg = Double.valueOf(data.get(i).getLongitude());
                id = data.get(i).getGlobalIdLocal();
                try {
                    List<Address> locations = geocoder.getFromLocation(lt,lg,1);
                    if(!locations.isEmpty()){
                        String city_name = locations.get(0).getAdminArea();
                        if(info.getCurrentLocation() == id){
                            location_name.setText(city_name);
                            max_temperature.setText(data.get(i).gettMax() + getString(R.string.temperature_symbol));
                            min_temperature.setText(data.get(i).gettMin() + getString(R.string.temperature_symbol));
                            precipitation.setText(data.get(i).getPrecipitaProb() + getString(R.string.percentage_symbol));
                            wind_direction.setText(data.get(i).getPredWindDir());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
