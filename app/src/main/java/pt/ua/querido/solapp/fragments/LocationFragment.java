package pt.ua.querido.solapp.fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import pt.ua.querido.solapp.R;
import pt.ua.querido.solapp.activities.MainActivity;
import pt.ua.querido.solapp.database.converter.Local;
import pt.ua.querido.solapp.database.entity.Location;
import pt.ua.querido.solapp.view_models.LocationViewModel;

public class LocationFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private LocationViewModel viewModel;


    // FOR DESIGN
    @BindView(R.id.location_name)
    TextView location_name;

    @BindView(R.id.lvLocal)
    ListView lvLocal;

    public LocationFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_info, container, false);
        ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).setCurrentScreen(2);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.configureDagger();
        this.configureViewModel(getArguments().getInt("ID"),getArguments().getString("name"));
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    private void configureDagger(){
        AndroidSupportInjection.inject(this);
    }

    private void configureViewModel(Integer id, String name){
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LocationViewModel.class);
        viewModel.init(id);
        viewModel.getInfo().observe(this, info -> updateUI(info,name));
    }

    // -----------------
    // UPDATE UI
    // -----------------

    private void updateUI(@Nullable Location info, String name){
        if (info != null){
            List<Local> data = info.getData();

            location_name.setText(getResources().getString(R.string.weather_in) + " " + name);

            ArrayList<LocalOnSteroids> arrayOfLocals = new ArrayList<>();
            String[] days_of_week = getResources().getStringArray(R.array.days_of_week);
            int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

            LocalAdapter adapter = new LocalAdapter(getContext(), arrayOfLocals);
            lvLocal.setAdapter(adapter);

            for(Local l : data){
                arrayOfLocals.add(new LocalOnSteroids(l,days_of_week[(dayOfWeek-2)%(days_of_week.length)]));
                dayOfWeek++;
            }
        }
    }

    private class LocalOnSteroids {

        private String dayOfWeek;
        private Local local;

        public LocalOnSteroids(Local local, String dayOfWeek) {
            this.local = local;
            this.dayOfWeek = dayOfWeek;
        }

        public String getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(String dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public Local getLocal() {
            return local;
        }

        public void setLocal(Local local) {
            this.local = local;
        }
    }

    public class LocalAdapter extends ArrayAdapter<LocalOnSteroids> {

        public LocalAdapter(Context context, ArrayList<LocalOnSteroids> locals) {
            super(context, 0, locals);


        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            LocalOnSteroids local = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_local, parent, false);
            }
            TextView day_week = convertView.findViewById(R.id.day_week);
            TextView max_temperature = convertView.findViewById(R.id.max_temperature);
            TextView min_temperature = convertView.findViewById(R.id.min_temperature);
            TextView precipitation = convertView.findViewById(R.id.precipitation);
            TextView wind_direction = convertView.findViewById(R.id.wind_direction);

            // Populate the data into the template view using the data object

            day_week.setText(local.getDayOfWeek());
            max_temperature.setText(local.getLocal().gettMax() + getString(R.string.temperature_symbol));
            min_temperature.setText(local.getLocal().gettMin() + getString(R.string.temperature_symbol));
            precipitation.setText(local.getLocal().getPrecipitaProb() + getString(R.string.percentage_symbol));
            wind_direction.setText(local.getLocal().getPredWindDir());
            // Return the completed view to render on screen
            return convertView;
        }
    }

}