package ada.osc.taskie;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import ada.osc.taskie.database.DatabaseHelper;
import ada.osc.taskie.model.Task;

/**
 * Created by avukelic on 07-May-18.
 */
public class TaskDao {
    private static Dao<Task,String> instance = null;
    private static DatabaseHelper databaseHelper = null;
    protected TaskDao() {
        // Exists only to defeat instantiation.
    }
    public static Dao<Task,String> getInstance(Context context) {
        if(instance == null) {
            try {
                instance = getHelper(context).getTaskDao();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
    // This is how, DatabaseHelper can be initialized for future use
    private static DatabaseHelper getHelper(Context context) {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return databaseHelper;
    }
}
