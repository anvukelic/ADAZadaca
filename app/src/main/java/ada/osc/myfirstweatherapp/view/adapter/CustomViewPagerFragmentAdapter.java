package ada.osc.myfirstweatherapp.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ada.osc.myfirstweatherapp.model.LocationWrapper;
import ada.osc.myfirstweatherapp.view.locations.WeatherFragment;

public class CustomViewPagerFragmentAdapter extends FragmentPagerAdapter {
    private final List<LocationWrapper> mCitiesList = new ArrayList<>();

    public CustomViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return WeatherFragment.newInstance(mCitiesList.get(position).getLocation());
    }

    public String getItemLocation(int position){
        return mCitiesList.get(position).getLocation();
    }

    @Override
    public int getCount() {
        return mCitiesList.size();
    }

    public void refreshData(List<LocationWrapper> dataSource) {
        this.mCitiesList.clear();
        this.mCitiesList.addAll(dataSource);
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mCitiesList.get(position).getLocation();
    }
}