package ada.osc.taskie.view.task.fragments;


import android.app.AlertDialog;
import android.app.DialogFragment;
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

import ada.osc.taskie.Consts;
import ada.osc.taskie.R;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskList;
import ada.osc.taskie.networking.ApiService;
import ada.osc.taskie.networking.RetrofitUtil;
import ada.osc.taskie.util.RecyclerItemTouchHelper;
import ada.osc.taskie.util.SharedPrefsUtil;
import ada.osc.taskie.view.task.TaskActivity;
import ada.osc.taskie.view.task.TaskAdapter;
import ada.osc.taskie.view.task.TaskClickListener;
import ada.osc.taskie.view.task.TaskDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllTasksFragment extends Fragment implements  RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {


    public AllTasksFragment() {
        // Required empty public constructor
    }


    @BindView(R.id.recycler_view_tasks)
    RecyclerView mTasksRecyclerView;
    private TaskAdapter mTaskAdapter;
    TaskClickListener mListener;


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
        ((TaskActivity)getActivity()).setFragmentRefreshListener(new FragmentRefreshListener() {
            @Override
            public void onCreateRefresh() {
                getTasksFromServer();
            }
        });
        getTasksFromServer();
    }

    private void setUpTaskSwipe() {
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mTasksRecyclerView);
    }

    //RecyclerView and item actions
    private void setUpTaskRecyclerView() {
        mTaskAdapter = new TaskAdapter(mListener);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getActivity());
        mTasksRecyclerView.setLayoutManager(llm);
        mTasksRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mTasksRecyclerView.setAdapter(mTaskAdapter);
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
                /*TaskHelper.updatePriorityOnClick(task, taskDao);
                mTaskAdapter.refreshData(getTasks());*/
            }

            @Override
            public void onStatusSwitchChange(Task task) {
                /*TaskHelper.updateStatusOnSwitchChange(task, taskDao);*/
            }

        };
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        makeTaskFavorite(mTaskAdapter.getItem(position));
        mTaskAdapter.removeItem(position);
    }

    //Server side actions with tasks
    //Get all tasks
    private void getTasksFromServer() {
        Retrofit retrofit = RetrofitUtil.createRetrofit();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<TaskList> taskListCall = apiService
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
    private void askForDeleteConfirmation(final Task task){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete '" + task.getTitle() + "' task");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println(task.getId());
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
        Call deleteTaskCall = apiService.deleteTask(SharedPrefsUtil.getPreferencesField(getActivity()
                , SharedPrefsUtil.TOKEN), taskId);
        deleteTaskCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                getTasksFromServer();
                Toast.makeText(getActivity(), "Task deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            }
        });

    }
    //Activate on swipe right, make swiped item favorite
    private void makeTaskFavorite(Task task) {
        Retrofit retrofit = RetrofitUtil.createRetrofit();
        ApiService apiService = retrofit.create(ApiService.class);

        Call faveTaskCall = apiService.faveTask(SharedPrefsUtil.getPreferencesField(getActivity()
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
    private void showTaskDialogFragment(int action, Task task) {
        DialogFragment createTaskDialogFragment = new TaskDialogFragment();
        Bundle args = new Bundle();
        args.putInt("action", action);
        if (action == Consts.UPDATE_ACTION) {
            System.out.println(task.getId());
            args.putSerializable("task", task);
        }
        createTaskDialogFragment.setArguments(args);
        createTaskDialogFragment.show(getActivity().getFragmentManager(), TaskActivity.CREATE_TASK_TAG);
    }



}
