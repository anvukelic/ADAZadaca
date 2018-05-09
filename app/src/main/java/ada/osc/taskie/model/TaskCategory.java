package ada.osc.taskie.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

/**
 * Created by avukelic on 07-May-18.*/


@DatabaseTable(tableName = "task_category")
public class TaskCategory {

    @DatabaseField(id = true, columnName = "id")
    private String mId;
    @DatabaseField(columnName = "task", foreign = true)
    private Task mTask;
    @DatabaseField(columnName = "category", foreign = true)
    private Category mCategory;

    public TaskCategory() {
        mId = UUID.randomUUID().toString();
    }

    public TaskCategory(Task mTask, Category mCategory) {
        mId = UUID.randomUUID().toString();
        this.mTask = mTask;
        this.mCategory = mCategory;
    }

    public Task getTask() {
        return mTask;
    }

    public void setTask(Task mTask) {
        this.mTask = mTask;
    }

    public Category getCategory() {
        return mCategory;
    }

    public void setCategory(Category mCategory) {
        this.mCategory = mCategory;
    }
}
