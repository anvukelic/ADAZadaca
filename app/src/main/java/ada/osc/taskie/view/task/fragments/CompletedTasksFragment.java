package ada.osc.taskie.view.task.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import ada.osc.taskie.App;
import ada.osc.taskie.R;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskList;
import ada.osc.taskie.util.SharedPrefsUtil;
import ada.osc.taskie.view.task.OnItemLongClickListener;
import ada.osc.taskie.view.task.TaskAdapter;
import ada.osc.taskie.view.task.TaskClickListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompletedTasksFragment extends Fragment {


    public CompletedTasksFragment() {
        // Required empty public constructor
    }


    @BindView(R.id.recycler_view_tasks)
    RecyclerView mTasksRecyclerView;
    private TaskAdapter mTaskAdapter;

    OnItemLongClickListener onItemLongClickListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setUpTaskClickListener();
        setUpTaskRecyclerView();
        getCompletedTasks();
    }

    //RecyclerView and item actions
    private void setUpTaskRecyclerView() {
        mTaskAdapter = new TaskAdapter(onItemLongClickListener);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getActivity());
        mTasksRecyclerView.setLayoutManager(llm);
        mTasksRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mTasksRecyclerView.setAdapter(mTaskAdapter);
        getCompletedTasks();
    }

    private void setUpTaskClickListener() {
        onItemLongClickListener = new OnItemLongClickListener() {
            @Override
            public boolean deleteTask(Task task) {
                askForDeleteConfirmation(task);
                return true;
            }
        };
    }


    //Server side actions with tasks
    //Get all tasks
    private void getCompletedTasks() {
        Call<TaskList> taskListCall = App.getApiService()
                .getCompletedTasks(SharedPrefsUtil.getPreferencesField(getActivity()
                        , SharedPrefsUtil.TOKEN));
        taskListCall.enqueue(new Callback<TaskList>() {
            @Override
            public void onResponse(Call<TaskList> call, Response<TaskList> response) {
                if (response.isSuccessful()) {
                    mTaskAdapter.refreshData(response.body().mTaskList);
                }
            }

            @Override
            public void onFailure(Call<TaskList> call, Throwable t) {
            }
        });
    }

    //Delete
    private void askForDeleteConfirmation(final Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete '" + task.getTitle() + "' task");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTaskOnServer(task.getId());
                getCompletedTasks();
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
        Call deleteTaskCall = App.getApiService().deleteTask(SharedPrefsUtil.getPreferencesField(getActivity()
                , SharedPrefsUtil.TOKEN), taskId);
        deleteTaskCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                getCompletedTasks();
                Toast.makeText(getActivity(), "Task deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            }
        });

    }
}
