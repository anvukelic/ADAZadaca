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
import android.widget.PopupMenu;
import android.widget.Spinner;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ada.osc.taskie.NewTaskDialogFragment;
import ada.osc.taskie.R;
import ada.osc.taskie.TaskClickListener;
import ada.osc.taskie.database.DatabaseHelper;
import ada.osc.taskie.model.Task;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskActivity extends AppCompatActivity implements NewTaskDialogFragment.OnAddListener {

    public static final String CREATE_TASK_TAG = "createTaskDialog";
    public static final int CREATE_TASK_ACTION = 10;
    public static final int UPDATE_TASK_ACTION = 20;
    public static final int SORT_ALL = 0;
    public static final int SORT_ASC = 1;
    public static final int SORT_DESC = 2;
    public static final int FILTER_ALL = 0;
    public static final int FILTER_DONE = 1;
    public static final int FILTER_NOT_DONE = 2;


    private int mSortPriorityType;
    private int mFilterStatusType = 0;

    @BindView(R.id.spinner_task_priority)
    Spinner mSortType;
    @BindView(R.id.recycler_view_tasks)
    RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;

    private DatabaseHelper databaseHelper = null;
    private Dao<Task, String> taskDao;

    TaskClickListener mListener = new TaskClickListener() {
        @Override
        public void onClick(final Task task, int itemId, View view) {
            switch (itemId) {
                case R.id.textview_recyclerview_item_menu:
                    showPopUp(task, view);
                    break;
                case R.id.imageview_recyclerview_priority:
                    break;
                default:
            }
            mAdapter.refreshData(getTasksFromDb());

        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        try {
            taskDao = getHelper().getTaskDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setUpRecyclerView();
        setupPrioritySpinner();
        setupFloatingButton();

    }


    /*
     *Methods called in onCreate
     */
    //Setup floating button to showTaskDialogFragment(create action)
    private void setupFloatingButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTaskDialogFragment(CREATE_TASK_ACTION);
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


    // This is how, DatabaseHelper can be initialized for future use
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
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

    //Show task dialog
    //Two different calls: create and update
    private void showTaskDialogFragment(int action) {
        DialogFragment createTaskDialogFragment = new NewTaskDialogFragment();
        Bundle args = new Bundle();
        args.putInt("action", action);
        createTaskDialogFragment.setArguments(args);
        createTaskDialogFragment.show(this.getFragmentManager(), CREATE_TASK_TAG);
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
                mAdapter.refreshData(getTasksFromDb());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

    //On item options button click pop up window
    //Check as done option
    //showTaskDialogFragment(update action)
    private void showPopUp(final Task task, View v) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.inflate(R.menu.popup_item_options_menu);
        if (task.isDone()) {
            popup.getMenu().getItem(1).setTitle(R.string.check_as_not_done);
        } else {
            popup.getMenu().getItem(1).setTitle(R.string.check_as_done);
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_delete_task:
                        try {
                            DeleteBuilder<Task, String> deleteBuilder = taskDao.deleteBuilder();
                            deleteBuilder.where().eq("id", task.getId());
                            deleteBuilder.delete();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        mAdapter.refreshData(getTasksFromDb());
                        break;
                    case R.id.menu_check_as_done:
                        //mTaskRepository.changeTaskStatus(task);
                        mAdapter.refreshData(getTasksFromDb());
                        break;
                    case R.id.menu_update:
                        int taskId = getTasksFromDb().indexOf(task);
                        showTaskDialogFragment(UPDATE_TASK_ACTION);
                        break;
                }
                return false;
            }
        });
        //displaying the popup
        popup.show();
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
                        mAdapter.refreshData(getTasksFromDb());
                        break;
                    case "Done":
                        item.setTitle(R.string.tasks_not_finished);
                        mFilterStatusType = 2;
                        mAdapter.refreshData(getTasksFromDb());
                        break;
                    case "Not done":
                        item.setTitle(R.string.all_tasks);
                        mFilterStatusType = 0;
                        mAdapter.refreshData(getTasksFromDb());
                        break;
                }
                break;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskAdd() {
        mAdapter.refreshData(getTasksFromDb());
    }

    private List<Task> getTasksFromDb() {
        QueryBuilder<Task, String> queryBuilder = taskDao.queryBuilder();
        queryBuilder = sortTasksByPriority(queryBuilder);
        return filterTasksByStatus(queryBuilder);
    }

    private List<Task> filterTasksByStatus(QueryBuilder<Task, String> queryBuilder) {
        try {
            switch (mFilterStatusType) {
                case FILTER_DONE:
                    return queryBuilder.where().eq("status", true).query();
                case SORT_DESC:
                    return queryBuilder.where().eq("status", false).query();
                default:
                    return queryBuilder.query();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private QueryBuilder<Task, String> sortTasksByPriority(QueryBuilder<Task, String> queryBuilder) {
        switch (mSortPriorityType) {
            case SORT_ASC:
                return queryBuilder.orderBy("priority", true);
            case SORT_DESC:
                return queryBuilder.orderBy("priority", false);
            default:
                return queryBuilder;
        }

    }


}
