package ada.osc.taskie.ui;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import ada.osc.taskie.NewTaskDialogFragment;
import ada.osc.taskie.R;
import ada.osc.taskie.TaskRepository;
import ada.osc.taskie.model.Task;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskActivity extends AppCompatActivity implements NewTaskDialogFragment.OnAddListener {


    public static final String DB_PREFS = "dbPrefs";
    public static final String TASK_JSON = "taskJson";

    public static final String CREATE_TASK_TAG = "creteTaskDialog";

    private TaskRepository mTaskRepository = TaskRepository.getInstance();

    @BindView(R.id.recycler_view_tasks)
    RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        //getSharedPreferences(DB_PREFS, MODE_PRIVATE).edit().clear().apply();
        getTasksFromSharedPreferences();
        setUpRecyclerView();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateTaskDialog();
            }
        });
    }

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        putTasksInSharedPreferences();
    }

    private void setUpRecyclerView() {
        mAdapter = new TaskAdapter(mTaskRepository.getTasks());
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showCreateTaskDialog() {
        DialogFragment createTaskDialogFragment = new NewTaskDialogFragment();
        createTaskDialogFragment.show(this.getFragmentManager(), CREATE_TASK_TAG);
    }

    private void putTasksInSharedPreferences() {
        Gson gson = new Gson();
        String json = gson.toJson(mTaskRepository.getTasks());
        System.out.println(json);
        getSharedPreferences(DB_PREFS, MODE_PRIVATE).edit().putString(TASK_JSON, json).apply();
        //getSharedPreferences(DB_PREFS, MODE_PRIVATE).edit().putString(TASK_JSON, "").apply();
    }

    private void getTasksFromSharedPreferences() {
        Gson gson = new Gson();
        List<Task> tasks;
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(DB_PREFS, Context.MODE_PRIVATE);
        String jsonPreferences = sharedPref.getString(TASK_JSON, "");
        System.out.println(jsonPreferences);
        Type type = new TypeToken<List<Task>>() {
        }.getType();
        tasks = gson.fromJson(jsonPreferences, type);
        mTaskRepository.getTasks().clear();
        if (jsonPreferences.trim().length()>0) {
            for (Task task : tasks) {
                mTaskRepository.save(task);
                System.out.println(task.getTitle());
            }
        }
    }

    @Override
    public void sendTaskData(String title, String description, int priority, Date date) {
        Task task = new Task(title,description,priority,date);
        mTaskRepository.save(task);
        mAdapter.refreshData(mTaskRepository.getTasks());
    }





}
