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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ada.osc.taskie.App;
import ada.osc.taskie.R;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskList;
import ada.osc.taskie.util.RecyclerItemTouchHelper;
import ada.osc.taskie.util.SharedPrefsUtil;
import ada.osc.taskie.view.task.OnItemLongClickListener;
import ada.osc.taskie.view.task.TaskActivity;
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
public class AllTasksFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {


    public AllTasksFragment() {
        // Required empty public constructor
    }


    @BindView(R.id.recycler_view_tasks)
    RecyclerView mTasksRecyclerView;
    private TaskAdapter mTaskAdapter;

    TaskClickListener mListener;
    OnItemLongClickListener onItemLongClickListener;

    OnItemUpdateListener taskUpdateListener;


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
        setUpTaskSwipe();
        setUpRefreshListener();
        getTasks();
    }

    //Refresh list after update or create
    private void setUpRefreshListener() {
        ((TaskActivity) getActivity()).setFragmentRefreshListener(new FragmentRefreshListener() {
            @Override
            public void onCreateRefresh() {
                getTasks();
            }
        });
    }

    private void setUpTaskSwipe() {
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mTasksRecyclerView);
    }

    //RecyclerView and item actions
    private void setUpTaskRecyclerView() {
        mTaskAdapter = new TaskAdapter(mListener,onItemLongClickListener, true);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getActivity());
        mTasksRecyclerView.setLayoutManager(llm);
        mTasksRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mTasksRecyclerView.setAdapter(mTaskAdapter);
        getTasks();
    }

    private void setUpTaskClickListener() {
        onItemLongClickListener = new OnItemLongClickListener() {
            @Override
            public boolean deleteTask(Task task) {
                askForDeleteConfirmation(task);
                return true;
            }
        };

        mListener = new TaskClickListener() {
            @Override
            public void onClick(Task task) {
                taskUpdateListener.taskUpdate(task);
            }

            @Override
            public void onPriorityChangeClick(Task task) {
                if(!task.isDone()){
                switch (task.getPriority()) {
                    case 1:
                        task.setPriority(2);
                        break;
                    case 2:
                        task.setPriority(3);
                        break;
                    case 3:
                        task.setPriority(1);
                        break;
                }
                changeTaskPriority(task);}
            }

            @Override
            public void onStatusSwitchChange(Task task) {
                changeTaskStatus(task);
                getTasks();
            }
        };
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            taskUpdateListener = (OnItemUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        makeTaskFavorite(mTaskAdapter.getItem(position));
        mTaskAdapter.removeItem(position);
    }

    //Server side actions with tasks
    //Get all tasks
    private void getTasks() {
        Call<TaskList> taskListCall = App.getApiService()
                .getTasks(SharedPrefsUtil.getPreferencesField(getActivity()
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
                getTasks();
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
                getTasks();
                Toast.makeText(getActivity(), "Task deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            }
        });

    }

    //Activate on swipe right, make swiped item favorite
    private void makeTaskFavorite(Task task) {
        Call faveTaskCall = App.getApiService().faveTask(SharedPrefsUtil.getPreferencesField(getActivity()
                , SharedPrefsUtil.TOKEN), task.getId());
        faveTaskCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Toast.makeText(getActivity(), "Task faved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            }
        });
    }

    private void changeTaskPriority(Task task) {
        Call postUpdateTaskCall = App.getApiService()
                .updateTask(SharedPrefsUtil.getPreferencesField(getActivity()
                        , SharedPrefsUtil.TOKEN), task);
        postUpdateTaskCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    getTasks();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            }
        });
    }

    private void changeTaskStatus(Task task) {
        Call changeStatusCall = App.getApiService().changeTaskStatus(SharedPrefsUtil.getPreferencesField(getActivity()
                , SharedPrefsUtil.TOKEN), task.getId());
        changeStatusCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                getTasks();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            }
        });
    }

}
