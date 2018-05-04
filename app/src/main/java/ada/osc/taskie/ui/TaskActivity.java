package ada.osc.taskie.ui;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import ada.osc.taskie.NewTaskDialogFragment;
import ada.osc.taskie.R;
import ada.osc.taskie.TaskClickListener;
import ada.osc.taskie.TaskRepository;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.persistence.FakeDatabase;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskActivity extends AppCompatActivity implements NewTaskDialogFragment.OnAddListener {


    public static final String DB_PREFS = "dbPrefs";
    public static final String TASK_JSON = "taskJson";
    public static final String CREATE_TASK_TAG = "createTaskDialog";
    public static final int CREATE_TASK_ACTION = 10;
    public static final int UPDATE_TASK_ACTION = 20;

    private TaskRepository mTaskRepository = TaskRepository.getInstance();

    private int mSortPriorityType;
    private int mFilterStatusType = 0;

    @BindView(R.id.spinner_task_priority)
    Spinner mFilter;
    @BindView(R.id.recycler_view_tasks)
    RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;

    TaskClickListener mListener = new TaskClickListener() {
        @Override
        public void onClick(final Task task, int itemId, View view) {
            switch (itemId) {
                case R.id.textview_recyclerview_item_menu:
                    showPopUp(task, view);
                    break;
                case R.id.imageview_recyclerview_priority:
                    mTaskRepository.updateTaskPriority(task);
                    break;
                default:
            }
            mAdapter.refreshData(getTasksFromRepository());

        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        //getSharedPreferences(DB_PREFS, MODE_PRIVATE).edit().clear().apply();
        setUpRecyclerView();
        setupPrioritySpinner();
        getTasksFromSharedPreferences();
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
                showTaskDialogFragment(0, CREATE_TASK_ACTION, null, null, null);
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

    //Get task list from SharedPreferences
    private void getTasksFromSharedPreferences() {
        Gson gson = new Gson();
        List<Task> tasks;
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(DB_PREFS, Context.MODE_PRIVATE);
        String jsonPreferences = sharedPref.getString(TASK_JSON, "");
        Type type = new TypeToken<List<Task>>() {
        }.getType();
        tasks = gson.fromJson(jsonPreferences, type);
        System.out.println(jsonPreferences);
        mTaskRepository.deleteAll();
        if (jsonPreferences.trim().length() > 0) {
            for (Task task : tasks) {
                mTaskRepository.save(task);
            }
        }
    }

    //Save list as json in SharedPreferences
    private void putTasksInSharedPreferences() {
        Gson gson = new Gson();
        String json = gson.toJson(mTaskRepository.getTasks(FakeDatabase.SORT_ALL, FakeDatabase.FILTER_ALL));
        System.out.println(json);
        getSharedPreferences(DB_PREFS, MODE_PRIVATE).edit().putString(TASK_JSON, json).apply();
    }


    //Lifecycle methods
    @Override
    protected void onStop() {
        super.onStop();
        putTasksInSharedPreferences();
    }

    //Show task dialog
    //Two different calls: create and update
    private void showTaskDialogFragment(int taskId, int action, String title, String description, Date date) {
        DialogFragment createTaskDialogFragment = new NewTaskDialogFragment();
        Bundle args = new Bundle();
        args.putInt("action", action);
        if (action == UPDATE_TASK_ACTION) {
            args.putInt("taskId", taskId);
            args.putString("title", title);
            args.putString("description", description);
            args.putLong("date", date.getTime());
        }
        createTaskDialogFragment.setArguments(args);
        createTaskDialogFragment.show(this.getFragmentManager(), CREATE_TASK_TAG);
    }

    //Setup priority spinner to show strings from R.array.priority_sort_type_list
    public void setupPrioritySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.prirority_sort_type_list, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFilter.setAdapter(adapter);
        mFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSortPriorityType = mFilter.getSelectedItemPosition();
                mAdapter.refreshData(getTasksFromRepository());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

    //Get data from dialog fragment
    //It depends what action user want to do(create or update)
    @Override
    public void sendTaskData(String title, String description, int priority, Date date, int action, int taskId) {
        if (action == CREATE_TASK_ACTION) {
            Task task = new Task(title, description, priority, date);
            mTaskRepository.save(task);
        } else {
            Task task = getTasksFromRepository().get(taskId);
            task.setTitle(title);
            task.setDescription(description);
            task.setDate(date);
        }
        mAdapter.refreshData(getTasksFromRepository());
    }

    //On item options button click pop up window
    //Check as done option
    //showTaskDialogFragment(update action)
    private void showPopUp(final Task task, View v) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.inflate(R.menu.popup_item_options_menu);
        if(task.isDone()){
            popup.getMenu().getItem(1).setTitle(R.string.check_as_not_done);
        } else {
            popup.getMenu().getItem(1).setTitle(R.string.check_as_done);
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_delete_task:
                        mTaskRepository.delete(task);
                        mAdapter.refreshData(getTasksFromRepository());
                        break;
                    case R.id.menu_check_as_done:
                        mTaskRepository.changeTaskStatus(task);
                        mAdapter.refreshData(getTasksFromRepository());
                        break;
                    case R.id.menu_update:
                        int taskId = getTasksFromRepository().indexOf(task);
                        showTaskDialogFragment(taskId, UPDATE_TASK_ACTION, task.getTitle(), task.getDescription(), task.getDate());
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
                        mAdapter.refreshData(getTasksFromRepository());
                        break;
                    case "Done":
                        item.setTitle(R.string.tasks_not_finished);
                        mFilterStatusType = 2;
                        mAdapter.refreshData(getTasksFromRepository());
                        break;
                    case "Not done":
                        item.setTitle(R.string.all_tasks);
                        mFilterStatusType = 0;
                        mAdapter.refreshData(getTasksFromRepository());
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

    private List<Task> getTasksFromRepository() {
        return mTaskRepository.getTasks(mSortPriorityType, mFilterStatusType);
    }


}
