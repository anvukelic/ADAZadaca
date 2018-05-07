package ada.osc.taskie.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.util.List;

import ada.osc.taskie.TaskHelper;
import ada.osc.taskie.R;
import ada.osc.taskie.TaskClickListener;
import ada.osc.taskie.TaskDao;
import ada.osc.taskie.database.DatabaseHelper;
import ada.osc.taskie.model.Task;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskActivity extends AppCompatActivity implements NewTaskDialogFragment.OnTaskChangeListener {

    public static final String CREATE_TASK_TAG = "createTaskDialog";
    public static final int CREATE_TASK_ACTION = 10;
    public static final int UPDATE_TASK_ACTION = 20;

    private int mSortPriorityType;
    private int mFilterStatusType = 0;

    @BindView(R.id.spinner_task_priority)
    Spinner mSortType;
    @BindView(R.id.recycler_view_tasks)
    RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;

    private DatabaseHelper databaseHelper = null;
    private Dao<Task, String> taskDao;
    TaskClickListener mListener;

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
        taskDao = TaskDao.getInstance(this);
        setUpTaskClickListener();
        setUpRecyclerView();
        setupPrioritySpinner();
        setupFloatingButton();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /*
         * You'll need this in your class to release the helper when done.
         */
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    //Setup floating button to showTaskDialogFragment(create action)
    private void setupFloatingButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTaskDialogFragment(CREATE_TASK_ACTION, null);
            }
        });
    }

    private void setUpRecyclerView() {
        mAdapter = new TaskAdapter(mListener);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setUpTaskClickListener() {
        mListener = new TaskClickListener() {
            @Override
            public void onClick(Task task) {
                showTaskDialogFragment(UPDATE_TASK_ACTION, task.getId());
                mAdapter.refreshData(getTasks());
            }

            @Override
            public boolean onLongClick(Task task) {
                TaskHelper.deleteTask(task, TaskActivity.this, taskDao);
                mAdapter.refreshData(getTasks());
                return true;
            }

            @Override
            public void onPriorityChangeClick(Task task) {
                TaskHelper.updatePriorityOnClick(task,taskDao);
                mAdapter.refreshData(getTasks());
            }

            @Override
            public void onStatusSwitchChange(Task task) {
                TaskHelper.updateStatusOnSwitchChange(task,taskDao);
            }

        };
    }

    //Setup priority spinner to show strings from R.array.priority_sort_type_list
    public void setupPrioritySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.prirority_sort_type_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSortType.setAdapter(adapter);
        mSortType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSortPriorityType = mSortType.getSelectedItemPosition();
                mAdapter.refreshData(getTasks());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //Show task dialog
    //Two different calls: create and update
    private void showTaskDialogFragment(int action, String taskId) {
        DialogFragment createTaskDialogFragment = new NewTaskDialogFragment();
        Bundle args = new Bundle();
        args.putInt("action", action);
        if (action == UPDATE_TASK_ACTION) {
            args.putString("taskId", taskId);
        }
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
                        item.setTitle(R.string.tasks_finished);
                        mFilterStatusType = 1;
                        mAdapter.refreshData(getTasks());
                        break;
                    case "Done":
                        item.setTitle(R.string.tasks_not_finished);
                        mFilterStatusType = 2;
                        mAdapter.refreshData(getTasks());
                        break;
                    case "Not done":
                        item.setTitle(R.string.all_tasks);
                        mFilterStatusType = 0;
                        mAdapter.refreshData(getTasks());
                        break;
                }
                break;
        }
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    //Called when NewTaskDialogFragment is close with positive button
    @Override
    public void onTaskChange(int action) {
        mAdapter.refreshData(getTasks());
    }

    public List<Task> getTasks(){
        return TaskHelper.getTasksFromDb(mFilterStatusType,mSortPriorityType,taskDao);
    }



}
