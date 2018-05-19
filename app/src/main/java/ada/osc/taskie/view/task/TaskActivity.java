package ada.osc.taskie.view.task;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import ada.osc.taskie.Consts;
import ada.osc.taskie.R;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.view.task.fragments.AllTasksFragment;
import ada.osc.taskie.view.task.fragments.CompletedTasksFragment;
import ada.osc.taskie.view.task.fragments.FavoriteTasksFragment;
import ada.osc.taskie.view.task.fragments.FragmentRefreshListener;
import ada.osc.taskie.view.task.fragments.OnItemUpdateListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskActivity extends AppCompatActivity implements TaskDialogFragment.OnTaskChangeListener, OnItemUpdateListener {

    public static final String CREATE_TASK_TAG = "createTaskDialog";

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    private FragmentRefreshListener fragmentRefreshListener;

    @BindView(R.id.fragmentContainer)
    FrameLayout fragmentContainer;
    @BindView(R.id.fab)
    FloatingActionButton fab;

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
        setUpTabLayout();
    }

    private void setUpTabLayout() {
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Favorite"));
        tabLayout.addTab(tabLayout.newTab().setText("Completed"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragmentContainer, new AllTasksFragment());
        ft.commit();
    }

    private void changeFragment(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                ft.replace(R.id.fragmentContainer, new AllTasksFragment());
                break;
            case 1:
                ft.replace(R.id.fragmentContainer, new FavoriteTasksFragment());
                break;
            case 2:
                ft.replace(R.id.fragmentContainer, new CompletedTasksFragment());
                break;
        }
        ft.addToBackStack(null);
        ft.commit();
    }

    @OnClick(R.id.fab)
    void onFabClick(){
        showTaskDialogFragment(Consts.CREATE_ACTION, null);
    }

    //Show task dialog
    //Two different calls: create and update
    private void showTaskDialogFragment(int action, Task task) {
        DialogFragment createTaskDialogFragment = new TaskDialogFragment();
        Bundle args = new Bundle();
        args.putInt("action", action);
        if (action == Consts.UPDATE_ACTION) {
            args.putSerializable("task", task);
        }
        createTaskDialogFragment.setArguments(args);
        createTaskDialogFragment.show(this.getFragmentManager(), CREATE_TASK_TAG);
    }

    //Called when TaskDialogFragment is close with positive button
    @Override
    public void onTaskChange(int action) {
        getFragmentRefreshListener().onCreateRefresh();
    }

    @Override
    public void taskUpdate(Task task) {
        showTaskDialogFragment(Consts.UPDATE_ACTION, task);
    }
}
