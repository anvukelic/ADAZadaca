package ada.osc.taskie.ui.category;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ada.osc.taskie.model.Category;
import ada.osc.taskie.model.Task;

/**
 * Created by avukelic on 07-May-18.
 */
public class CategoryHelper {

    public static List<Category> getCategories(Dao<Category,String> categoryDao) {
        QueryBuilder<Category, String> queryBuilder = categoryDao.queryBuilder();
        try {
            return queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    // Delete category from DB
    public static void deleteCategory(final Category category, Context context, final Dao<Category,String> categoryDao) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to delete '" + category.getClass() + "' category");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteBuilder<Category, String> deleteBuilder = categoryDao.deleteBuilder();
                try {
                    deleteBuilder.where().eq("id", category.getId());
                    deleteBuilder.delete();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}