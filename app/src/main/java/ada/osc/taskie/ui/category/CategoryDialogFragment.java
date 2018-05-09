package ada.osc.taskie.ui.category;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;

import ada.osc.taskie.Consts;
import ada.osc.taskie.R;
import ada.osc.taskie.database.DatabaseHelper;
import ada.osc.taskie.model.Category;
import ada.osc.taskie.ui.task.TaskActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by avukelic on 28-Apr-18.
 */
public class CategoryDialogFragment extends DialogFragment {

    public interface onCategoryChangeListener {
        void onCategoryChange(int action);
    }

    Dao<Category, String> categoryDao;
    public onCategoryChangeListener mListener;
    private DatabaseHelper databaseHelper = null;

    Category categoryForUpdate;

    @BindView(R.id.edittext_category_dialog_name)
    EditText mName;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_category, null);
        ButterKnife.bind(this, view);
        try {
            databaseHelper = new DatabaseHelper(getActivity());
            categoryDao = databaseHelper.getCategoryDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (getArguments().getInt("action") == Consts.UPDATE_ACTION) {
            updateAction(getArguments().getString("categoryId"));
        }
        return createAlertDialog(view);
    }

    //Set title and set buttons on AlertDialogBuilder
    public AlertDialog.Builder createDialog(AlertDialog.Builder builder) {
        if (getArguments().getInt("action") == Consts.CREATE_ACTION) {
            builder.setTitle("Create new category")
                    .setPositiveButton(R.string.add_category, new DialogInterface.OnClickListener() {
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
            builder.setTitle("Update category")
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
                String name = mName.getText().toString();
                wantToCloseDialog = !checkFields(name);
                if (wantToCloseDialog) {
                    if (getArguments().getInt("action") == Consts.CREATE_ACTION) {
                        try {
                            categoryDao.create(new Category(name));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        updateCategory(categoryForUpdate.getId(), name);
                    }
                    mListener.onCategoryChange(getArguments().getInt("action"));
                    getDialog().dismiss();
                }
            }
        });
        return dialog;
    }

    private void updateCategory(String categoryId, String name) {
        try {
            UpdateBuilder<Category, String> updateBuilder = categoryDao.updateBuilder();
            updateBuilder.where().eq("id", categoryId);
            updateBuilder.updateColumnValue("name", name);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //Check if all fields are populated
    private Boolean checkFields(String name) {
        if (name.trim().length() > 20) {
            Toast.makeText(getActivity(), R.string.too_long_name, Toast.LENGTH_SHORT).show();
        }
        if (name.trim().length() == 0 || name.isEmpty()) {
            Toast.makeText(getActivity(), R.string.name_field_empty, Toast.LENGTH_SHORT).show();
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (onCategoryChangeListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }


    //Create dialog with task data in fields
    private void updateAction(String categoryId) {
        try {
            QueryBuilder<Category, String> queryBuilder = categoryDao.queryBuilder();
            categoryForUpdate = categoryDao.queryForFirst(queryBuilder.where().eq("id", categoryId).prepare());
            mName.setText(categoryForUpdate.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
