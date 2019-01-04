package pt.ua.querido.solapp.fragments;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import pt.ua.querido.solapp.R;
import pt.ua.querido.solapp.activities.MainActivity;
import pt.ua.querido.solapp.database.converter.Region;
import pt.ua.querido.solapp.database.entity.DayInfo;
import pt.ua.querido.solapp.markers.ClusterMarker;
import pt.ua.querido.solapp.markers.MyClusterManagerRenderer;
import pt.ua.querido.solapp.view_models.DayInfoViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class DayInfoFragment extends Fragment implements OnMapReadyCallback , ClusterManager.OnClusterItemInfoWindowClickListener{

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DayInfoViewModel viewModel;

    // FOR DESIGN
    @BindView(R.id.map)
    MapView mapView;

    @BindView(R.id.options_list)
    Spinner options_list;

    @BindView(R.id.azores_button)
    Button azores_button;

    @BindView(R.id.madeira_button)
    Button madeira_button;

    @BindView(R.id.mainland_button)
    Button mainland_button;

    private GoogleMap mMap;
    private static final LatLng CENTER_MAINLAND = new LatLng(39.561209, -7.939491);
    private static final LatLng CENTER_AZORES = new LatLng(38.659480, -28.256208);
    private static final LatLng CENTER_MADEIRA = new LatLng(32.869068, -16.637840);
    private ClusterManager<ClusterMarker> mClusterManager;
    private MyClusterManagerRenderer mClusterManagerRenderer;
    private HashMap<ClusterItem,Integer> clusterItems;


    public DayInfoFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_info, container, false);
        ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).setCurrentScreen(1);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        clusterItems = new HashMap<>();
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(getContext(), R.array.options_days, android.R.layout.simple_spinner_item);
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        options_list.setAdapter(staticAdapter);
        options_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        viewModel.init("0",getActivity());
                        viewModel.getInfo().observe(getActivity(), info -> updateUI(info));
                        break;
                    case 1:
                        viewModel.init("1",getActivity());
                        viewModel.getInfo().observe(getActivity(), info -> updateUI(info));
                        break;
                    case 2:
                        viewModel.init("2",getActivity());
                        viewModel.getInfo().observe(getActivity(), info -> updateUI(info));
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupThings();
        this.configureDagger();
        this.configureViewModel();
    }

    private void setupThings() {
        mainland_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(CENTER_MAINLAND));
            }
        });
        azores_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(CENTER_AZORES));
            }
        });
        madeira_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(CENTER_MADEIRA));
            }
        });

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
        viewModel.getInfo().observe(getActivity(), info -> updateUI(info));
    }

    // -----------------
    // UPDATE UI
    // -----------------

    private void updateUI(@Nullable DayInfo info){
        if (info != null){
            List<Region> data = info.getData();
            addMapMarkers(data);
        }
    }

    private void addMapMarkers(List<Region> data){
        if(mMap != null){

            if(mClusterManager == null){
                mClusterManager = new ClusterManager<>(getActivity().getApplicationContext(), mMap);
                mMap.setOnInfoWindowClickListener(mClusterManager);
                mClusterManager.setOnClusterItemInfoWindowClickListener(this);
            } else {
                mClusterManager.clearItems();
            }
            if(mClusterManagerRenderer == null){
                mClusterManagerRenderer = new MyClusterManagerRenderer(
                        getActivity(),
                        mMap,
                        mClusterManager
                );
                mClusterManager.setRenderer(mClusterManagerRenderer);
            }

            for(int i = 0; i < data.size(); i++){
                try{
                    String snippet = getString(R.string.more_info);

                    int avatar = 0;

                    int weather_id = data.get(i).getIdWeatherType();
                    switch(weather_id){
                        case 1:
                            avatar = R.drawable.clear_sky;
                            break;
                        case 2:
                            avatar = R.drawable.partly_cloudly;
                            break;
                        case 3:
                            avatar = R.drawable.sunny_intervals;
                            break;
                        case 4: //unknown
                            avatar = R.drawable.clear_sky;
                            break;
                        case 5:
                            avatar = R.drawable.cloudy;
                            break;
                        case 6:
                            avatar = R.drawable.showers_rain;
                            break;
                        case 7: //unknown
                            avatar = R.drawable.clear_sky;
                            break;
                        case 8: //unknown
                            avatar = R.drawable.clear_sky;
                            break;
                        case 9:
                            avatar = R.drawable.rain_showers;
                            break;


                    }

                    ClusterMarker newClusterMarker = new ClusterMarker(
                            new LatLng(Double.valueOf(data.get(i).getLatitude()), Double.valueOf(data.get(i).getLongitude())),
                            getString(R.string.Min) + data.get(i).gettMin() + " | " + getString(R.string.Max) + data.get(i).gettMax(),
                            snippet,
                            avatar,
                            data.get(i).getGlobalIdLocal()
                    );
                    mClusterManager.addItem(newClusterMarker);
                    clusterItems.put(newClusterMarker,data.get(i).getGlobalIdLocal());

                }catch (NullPointerException e){
                    Log.e("TAG", "addMapMarkers: NullPointerException: " + e.getMessage() );
                }

            }
            mClusterManager.cluster();

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMinZoomPreference(6);
        mMap.setMaxZoomPreference(10);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(CENTER_MAINLAND));
    }

    @Override
    public void onClusterItemInfoWindowClick(ClusterItem item) {
        double lt, lg;
        lt = item.getPosition().latitude;
        lg = item.getPosition().longitude;
        Geocoder geocoder = new Geocoder(getContext(),Locale.getDefault());
        try {
            List<Address> locations = geocoder.getFromLocation(lt,lg,1);
            if(!locations.isEmpty()){
                String locality = locations.get(0).getLocality();
                String subAdminArea = locations.get(0).getSubAdminArea();
                String adminArea = locations.get(0).getAdminArea();
                String city_name = null;
                if(locality == null && adminArea != null){
                    if(subAdminArea == null){
                        city_name = adminArea;
                    } else {
                        city_name = subAdminArea;
                    }
                } else if(locality != null && subAdminArea == null){
                    city_name = adminArea;
                } else {
                    city_name = locality;
                }
                if(city_name == null){
                    city_name = getResources().getString(R.string.unknown_location);
                }
                LocationFragment fragment = new LocationFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("ID",clusterItems.get(item));
                bundle.putString("name",city_name);
                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment, null)
                        .commit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
