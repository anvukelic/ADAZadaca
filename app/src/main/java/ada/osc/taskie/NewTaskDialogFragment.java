package ada.osc.taskie;

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

import java.sql.SQLException;
import java.util.Calendar;

import ada.osc.taskie.database.DatabaseHelper;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.ui.TaskActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by avukelic on 28-Apr-18.
 */
public class NewTaskDialogFragment extends DialogFragment {

    public interface OnAddListener {
        void onTaskAdd();
    }

    public OnAddListener mOnAddListener;

    private DatabaseHelper databaseHelper = null;

    @BindView(R.id.edittext_newtask_title)
    EditText mTitle;
    @BindView(R.id.edittext_newtask_description)
    EditText mDescription;
    @BindView(R.id.spinner_newtask_priority)
    Spinner mPriority;
    @BindView(R.id.calendarview_newtask_date)
    CalendarView mDate;

    Calendar selectedDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_creating_task, null);
        ButterKnife.bind(this, view);
        if (getArguments().getInt("action") == TaskActivity.CREATE_TASK_ACTION) {
            createAction();
        } else {
            updateAction(getArguments().getString("title"), getArguments().getString("description"), getArguments().getLong("date"));
        }
        selectedDate = Calendar.getInstance();
        selectedDate.setTimeInMillis(mDate.getDate());
        mDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate.set(year, month, dayOfMonth);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mTitle.getWindowToken(), 0);
            }
        });
        return createAlertDialog(view);
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
                wantToCloseDialog = !checkFields(title, description);
                if (wantToCloseDialog) {
                    try {
                        Dao<Task, String> taskDao = getHelper().getTaskDao();
                        taskDao.create(new Task(mTitle.getText().toString(),
                                mDescription.getText().toString(),
                                mPriority.getSelectedItemPosition(),
                                selectedDate.getTime()));
                        mOnAddListener.onTaskAdd();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    getDialog().dismiss();
                }
            }
        });
        return dialog;
    }

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

    //Set title and set buttons on AlertDialogBuilder
    public AlertDialog.Builder createDialog(AlertDialog.Builder builder) {
        if (getArguments().getInt("action") == TaskActivity.CREATE_TASK_ACTION) {
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
                if (daySelected < dayNow) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnAddListener = (OnAddListener) getActivity();
        } catch (ClassCastException e) {
        }
    }

    //On createDialog check what action user want to make
    //On updateAction populate fields with data from item to update
    private void createAction() {
        setupPrioritySpinner();
    }

    private void updateAction(String title, String description, Long date) {
        mTitle.setText(title);
        mDescription.setText(description);
        mDate.setDate(date);
        mPriority.setVisibility(View.GONE);

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

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        }
        return databaseHelper;
    }
}
