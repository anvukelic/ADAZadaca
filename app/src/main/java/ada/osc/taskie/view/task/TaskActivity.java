package ada.osc.taskie.view.task;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import ada.osc.taskie.Consts;
import ada.osc.taskie.R;
import ada.osc.taskie.database.DatabaseHelper;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskList;
import ada.osc.taskie.networking.ApiService;
import ada.osc.taskie.networking.RetrofitUtil;
import ada.osc.taskie.util.RecyclerItemTouchHelper;
import ada.osc.taskie.util.SharedPrefsUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaskActivity extends AppCompatActivity implements TaskDialogFragment.OnTaskChangeListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    public static final String CREATE_TASK_TAG = "createTaskDialog";


    private int mSortPriorityType;
    private int mFilterStatusType = 0;

    private DatabaseHelper databaseHelper = null;

    @BindView(R.id.spinner_task_priority)
    Spinner mSortType;
    @BindView(R.id.recycler_view_tasks)
    RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;

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
        setUpTaskClickListener();
        setUpTaskRecyclerView();
        setupPrioritySpinner();
        setupFloatingButton();
        try {
            databaseHelper = new DatabaseHelper(this);
            taskDao = databaseHelper.getTaskDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

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
    private void showTaskDialogFragment(int action, Task task) {
        DialogFragment createTaskDialogFragment = new TaskDialogFragment();
        Bundle args = new Bundle();
        args.putInt("action", action);
        if (action == Consts.UPDATE_ACTION) {
            Gson gson = new Gson();
            String taskObject = gson.toJson(task);
            args.putString("task", taskObject);
        }
        createTaskDialogFragment.setArguments(args);
        createTaskDialogFragment.show(this.getFragmentManager(), CREATE_TASK_TAG);
    }

    //Setup floating button to showTaskDialogFragment(create action)
    private void setupFloatingButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTaskDialogFragment(Consts.CREATE_ACTION, null);
            }
        });
    }

    //RecyclerView and item actions
    private void setUpTaskRecyclerView() {
        mAdapter = new TaskAdapter(mListener);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        getTasksFromServer();
    }

    private void setUpTaskClickListener() {
        mListener = new TaskClickListener() {
            @Override
            public void onClick(Task task) {
                showTaskDialogFragment(Consts.UPDATE_ACTION, task);
            }

            @Override
            public boolean onLongClick(Task task) {
                askForDeleteConfirmation(task);
                return true;
            }

            @Override
            public void onPriorityChangeClick(Task task) {
                TaskHelper.updatePriorityOnClick(task, taskDao);
                mAdapter.refreshData(getTasks());
            }

            @Override
            public void onStatusSwitchChange(Task task) {
                TaskHelper.updateStatusOnSwitchChange(task, taskDao);
            }

        };
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        makeTaskFavorite(mAdapter.getItem(position));
        mAdapter.removeItem(position);
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
                        mAdapter.refreshData(getTasks());
                        break;
                    case "Done":
                        item.setTitle(R.string.not_done);
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




    //Activate on swipe right, make swiped item favorite
    private void makeTaskFavorite(Task task) {
        Retrofit retrofit = RetrofitUtil.createRetrofit();
        ApiService apiService = retrofit.create(ApiService.class);

        Call faveTaskCall = apiService.faveTask(SharedPrefsUtil.getPreferencesField(this
                , SharedPrefsUtil.TOKEN), task.getId());
        faveTaskCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Toast.makeText(TaskActivity.this, "Task faved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            }
        });
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

    //Called when TaskDialogFragment is close with positive button
    @Override
    public void onTaskChange(int action) {
        getTasksFromServer();
    }

    public List<Task> getTasks() {
        return TaskHelper.getTasksFromDb(mFilterStatusType, mSortPriorityType, taskDao);
    }

    //Server side actions with tasks
    //Get all tasks
    private void getTasksFromServer() {
        Retrofit retrofit = RetrofitUtil.createRetrofit();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<TaskList> taskListCall = apiService
                .getTasks(SharedPrefsUtil.getPreferencesField(this
                        , SharedPrefsUtil.TOKEN));
        taskListCall.enqueue(new Callback<TaskList>() {
            @Override
            public void onResponse(Call<TaskList> call, Response<TaskList> response) {
                if (response.isSuccessful()) {
                    mAdapter.refreshData(response.body().mTaskList);
                }
            }

            @Override
            public void onFailure(Call<TaskList> call, Throwable t) {
            }
        });
    }

    //Delete
    private void askForDeleteConfirmation(final Task task){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete '" + task.getTitle() + "' task");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTaskOnServer(task.getId());
                getTasksFromServer();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void deleteTaskOnServer(String taskId) {
        Retrofit retrofit = RetrofitUtil.createRetrofit();
        ApiService apiService = retrofit.create(ApiService.class);
        Call deleteTaskCall = apiService.deleteTask(SharedPrefsUtil.getPreferencesField(this
                , SharedPrefsUtil.TOKEN), taskId);
        deleteTaskCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                getTasksFromServer();
                Toast.makeText(TaskActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            }
        });

    }
}
