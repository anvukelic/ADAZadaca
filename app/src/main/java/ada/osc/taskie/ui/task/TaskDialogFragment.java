package ada.osc.taskie.ui.task;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import ada.osc.taskie.Consts;
import ada.osc.taskie.R;
import ada.osc.taskie.database.DatabaseHelper;
import ada.osc.taskie.model.Task;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by avukelic on 28-Apr-18.
 */
public class TaskDialogFragment extends DialogFragment {

    public interface OnTaskChangeListener {
        void onTaskChange(int action);
    }

    public static final String TASK_PREFS = "TaskiePrefs";
    public static final String PRIORITY_PREF = "priorityOnCreate";

    private Task taskForUpdate;
    Dao<Task, String> taskDao;

    public OnTaskChangeListener mListener;

    private DatabaseHelper databaseHelper = null;

    @BindView(R.id.edittext_task_dialog_title)
    EditText mTitle;
    @BindView(R.id.edittext_task_dialog_description)
    EditText mDescription;
    @BindView(R.id.spinner_task_dialog_priority)
    Spinner mPriority;
    @BindView(R.id.calendarview_task_dialog_date)
    CalendarView mDate;

    Calendar selectedDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_task, null);
        ButterKnife.bind(this, view);
        try {
            databaseHelper = new DatabaseHelper(getActivity());
            taskDao = databaseHelper.getTaskDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (getArguments().getInt("action") == Consts.CREATE_ACTION) {
            createAction();
        } else {
            updateAction(getArguments().getString("taskId"));
        }
        selectedDate = Calendar.getInstance();
        selectedDate.setTimeInMillis(mDate.getDate());
        mDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate.set(year, month, dayOfMonth);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(mTitle.getWindowToken(), 0);
            }
        });
        return createAlertDialog(view);
    }

    //Set title and set buttons on AlertDialogBuilder
    public AlertDialog.Builder createDialog(AlertDialog.Builder builder) {
        if (getArguments().getInt("action") == Consts.CREATE_ACTION) {
            builder.setTitle("Create new task")
                    .setPositiveButton(R.string.add_newtask, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getDialog().dismiss();
                        }
                    });
        } else {
            builder.setTitle("Update task")
                    .setPositiveButton(R.string.update_task, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getDialog().dismiss();
                        }
                    });
        }
        return builder;
    }

    //Build AlertDialog
    //Override positive button to prevent closing DialogFragment when any field is empty
    //Or date is before today
    public AlertDialog createAlertDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder = createDialog(builder);
        AlertDialog dialog = builder.create();
        dialog.show();
        //Override onClick to prevent closing DialogFragment
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog;
                String title = mTitle.getText().toString();
                String description = mTitle.getText().toString();
                int priority = mPriority.getSelectedItemPosition();
                Date date = selectedDate.getTime();
                wantToCloseDialog = !checkFields(title, description);
                if (wantToCloseDialog) {
                    if (getArguments().getInt("action") == Consts.CREATE_ACTION) {
                        try {
                            taskDao.create(new Task(title, description, priority, date));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        updateTask(taskForUpdate.getId(), title, description, priority, date);
                    }
                    mListener.onTaskChange(getArguments().getInt("action"));
                    getActivity().getSharedPreferences(TASK_PREFS, Context.MODE_PRIVATE)
                            .edit().putInt(PRIORITY_PREF, priority).apply();
                    getDialog().dismiss();
                }
            }
        });
        return dialog;
    }

    private void updateTask(String taskId, String title, String description, int priority, Date date) {
        try {
            UpdateBuilder<Task, String> updateBuilder = taskDao.updateBuilder();
            updateBuilder.where().eq("id", taskId);
            updateBuilder.updateColumnValue("title", title);
            updateBuilder.updateColumnValue("description", description);
            updateBuilder.updateColumnValue("priority", priority);
            updateBuilder.updateColumnValue("date", date);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //Check if all fields are populated
    private Boolean checkFields(String title, String description) {
        if (title.trim().length() > 30) {
            Toast.makeText(getActivity(), R.string.too_long_title, Toast.LENGTH_SHORT).show();
        } else if (description.trim().length() > 150) {
            Toast.makeText(getActivity(), R.string.too_long_description, Toast.LENGTH_SHORT).show();
        } else if (title.trim().length() == 0 || title.isEmpty()) {
            Toast.makeText(getActivity(), R.string.no_title, Toast.LENGTH_SHORT).show();
        } else if (description.trim().length() == 0 || description.isEmpty()) {
            Toast.makeText(getActivity(), R.string.no_description, Toast.LENGTH_SHORT).show();
        } else if (!isValidDate()) {
            Toast.makeText(getActivity(), R.string.wrong_date, Toast.LENGTH_SHORT).show();
        } else {
            return false;
        }
        return true;
    }

    //Setup priority spinner to show strings from R.array.priority_level_list
    public void setupPrioritySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.prirority_level_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPriority.setAdapter(adapter);
    }

    //Check if date is in past
    public boolean isValidDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mDate.getDate());
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTimeInMillis(selectedDate.getTimeInMillis());
        int yearSelected = cal.get(Calendar.YEAR);
        int monthSelected = cal.get(Calendar.MONTH);
        int daySelected = cal.get(Calendar.DAY_OF_MONTH);
        if (yearSelected <= yearNow) {
            if (monthSelected <= monthNow) {
                return daySelected >= dayNow;
            }
        }
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnTaskChangeListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    //Create dialog with empty fields
    private void createAction() {
        setupPrioritySpinner();
        mPriority.setSelection(getActivity().getSharedPreferences(TASK_PREFS, Context.MODE_PRIVATE).getInt(PRIORITY_PREF, 0));
    }

    //Create dialog with task data in fields
    private void updateAction(String taskId) {
        setupPrioritySpinner();
        try {
            QueryBuilder<Task, String> queryBuilder = taskDao.queryBuilder();
            taskForUpdate = taskDao.queryForFirst(queryBuilder.where().eq("id", taskId).prepare());
            mTitle.setText(taskForUpdate.getTitle());
            mDescription.setText(taskForUpdate.getDescription());
            mDate.setDate(taskForUpdate.getDate().getTime());
            mPriority.setSelection(taskForUpdate.getPriority());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
