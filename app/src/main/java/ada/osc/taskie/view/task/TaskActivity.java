package ada.osc.taskie.view.task;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ada.osc.taskie.Consts;
import ada.osc.taskie.R;
import ada.osc.taskie.database.DatabaseHelper;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.view.task.fragments.AllTasksFragment;
import ada.osc.taskie.view.task.fragments.FavoriteTasksFragment;
import ada.osc.taskie.view.task.fragments.FragmentRefreshListener;
import ada.osc.taskie.view.task.fragments.TasksPagerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskActivity extends AppCompatActivity implements TaskDialogFragment.OnTaskChangeListener{

    public static final String CREATE_TASK_TAG = "createTaskDialog";
    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    private FragmentRefreshListener fragmentRefreshListener;


    private int mSortPriorityType;
    private int mFilterStatusType = 0;


    @BindView(R.id.spinner_task_priority)
    Spinner mSortType;

    private DatabaseHelper databaseHelper = null;
    private Dao<Task, String> taskDao;

    @BindView(R.id.fragmentContainer)
    ViewPager mViewPager;
    private TasksPagerAdapter pagerAdapter;

    /**
     * Lifecycle methods
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        setUpPrioritySpinner();
        setUpFloatingButton();
        try {
            databaseHelper = new DatabaseHelper(this);
            taskDao = databaseHelper.getTaskDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setUpPagerAdapter();
    }

    private void setUpPagerAdapter() {
        pagerAdapter = new TasksPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }
    //Setup floating button to showTaskDialogFragment(create action)
    private void setUpFloatingButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTaskDialogFragment(Consts.CREATE_ACTION);
            }
        });
    }
    //Setup priority spinner to show strings from R.array.priority_sort_type_list
    public void setUpPrioritySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.prirority_sort_type_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSortType.setAdapter(adapter);
        mSortType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSortPriorityType = mSortType.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        /*
         * You'll need this in your class to release the helper when done.
         */
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    //Show task dialog
    //Two different calls: create and update
    private void showTaskDialogFragment(int action) {
        DialogFragment createTaskDialogFragment = new TaskDialogFragment();
        Bundle args = new Bundle();
        args.putInt("action", action);
        createTaskDialogFragment.setArguments(args);
        createTaskDialogFragment.show(this.getFragmentManager(), CREATE_TASK_TAG);
    }

    //Toolbar methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.toolbar_task_status:
                switch (item.getTitle().toString()) {
                    case "All tasks":
                        item.setTitle(R.string.done);
                        mFilterStatusType = 1;
                        break;
                    case "Done":
                        item.setTitle(R.string.not_done);
                        mFilterStatusType = 2;
                        ;
                        break;
                    case "Not done":
                        item.setTitle(R.string.all_tasks);
                        mFilterStatusType = 0;
                        break;
                }
                break;
        }
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    //Called when TaskDialogFragment is close with positive button
    @Override
    public void onTaskChange(int action) {
        getFragmentRefreshListener().onCreateRefresh();
    }

}
