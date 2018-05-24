package ada.osc.myfirstweatherapp.view.locations;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import java.util.List;

import ada.osc.myfirstweatherapp.App;
import ada.osc.myfirstweatherapp.presentation.LocationPresenter;
import ada.osc.myfirstweatherapp.utils.AlertDialogUtils;
import ada.osc.myfirstweatherapp.view.adapter.CustomViewPagerFragmentAdapter;
import ada.osc.myfirstweatherapp.R;
import ada.osc.myfirstweatherapp.model.LocationWrapper;
import ada.osc.myfirstweatherapp.view.newLocation.AddNewLocationActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class LocationsActivity extends AppCompatActivity implements LocationsContract.View, AlertDialogUtils.OnAlertDialogButtonClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_activity_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_activity_navigation_view)
    NavigationView navigationView;
    @BindView(R.id.main_activity_view_pager)
    ViewPager viewPager;
    private CustomViewPagerFragmentAdapter adapter;

    private Menu drawerMenu;
    private SubMenu drawerSubMenu;

    private LocationsContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        ButterKnife.bind(this);
        presenter = new LocationPresenter();
        presenter.setView(this);
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLocationsInAdapter();
        addLocationsOnSubMenu();
    }

    private void addLocationsOnSubMenu() {
        drawerSubMenu.clear();
        if (adapter.getCount() > 0) {
            for (int i = 0; i < adapter.getCount(); i++) {
                drawerSubMenu.add(Menu.NONE, i, Menu.NONE, adapter.getItemLocation(i));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (android.R.id.home):
                drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initUI() {
        setUpLocationAdapter();
        initToolbar();
        initNavigationDrawer();
        initDrawerLocationSubMenu();
    }

    private void initDrawerLocationSubMenu() {
        drawerMenu = navigationView.getMenu();
        drawerSubMenu = drawerMenu.addSubMenu("Locations");
    }

    private void initNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                presenter.onDrawerItemClicked(item.getItemId());
                return false;
            }
        });
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.main_activity_title);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        toolbar.setNavigationIcon(R.drawable.ic_navigation_drawer);
    }

    private void setUpLocationAdapter() {
        adapter = new CustomViewPagerFragmentAdapter(getSupportFragmentManager());
        setLocationsInAdapter();
    }

    private void setLocationsInAdapter() {
        presenter.getLocations();
    }

    @Override
    public void showLocations(List<LocationWrapper> locations) {
        adapter.refreshData(locations);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onLocationRemove() {
        drawerLayout.closeDrawers();
        setUpLocationAdapter();
        addLocationsOnSubMenu();
    }

    @Override
    public void onNewLocationDrawerItemClicked() {
        startActivity(new Intent(this, AddNewLocationActivity.class));
        drawerLayout.closeDrawers();
    }

    @Override
    public void onLocationDrawerItemClicked(int itemId) {
        AlertDialogUtils.askForDeleteAlertDialog(this, adapter.getItemLocation(itemId), this);
    }

    @Override
    public void onAlertDialogButtonClick(String location) {
        presenter.deleteLocation(location);
    }
}