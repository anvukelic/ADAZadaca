package ada.osc.taskie.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by avukelic on 28-Apr-18.
 */
@DatabaseTable(tableName = Task.TASK_TABLE)
public class Task {

    public static final String TASK_TABLE = "tasks";
    public static final String TASK_ID = "id";
    public static final String TASK_TITLE = "title";
    public static final String TASK_DESCRIPTION = "description";
    public static final String TASK_PRIORITY = "priority";
    public static final String TASK_DATE = "date";
    public static final String TASK_STATUS = "status";

    @Expose
    @SerializedName("id")
    @DatabaseField(columnName = TASK_ID, id = true)
    private String mId;
    @Expose
    @SerializedName("title")
    @DatabaseField(columnName = TASK_TITLE)
    private String mTitle;
    @DatabaseField(columnName = TASK_DESCRIPTION)
    @Expose
    @SerializedName("content")
    private String mDescription;
    @DatabaseField(columnName = TASK_PRIORITY)
    @Expose
    @SerializedName("taskPriority")
    private int mPriority;
    @DatabaseField(columnName = TASK_DATE)
    @Expose
    @SerializedName("dueDate")
    private String mDate;
    @DatabaseField(columnName = TASK_STATUS)
    private boolean mCompleted;

    public Task() {
    }

    public Task(String mTitle, String mDescription, int mPriority, String mDate) {
        mId = UUID.randomUUID().toString();
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mPriority = mPriority;
        this.mDate = mDate;
        mCompleted = false;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int mPriority) {
        this.mPriority = mPriority;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public boolean isDone() {
        return mCompleted;
    }

    public void setDone(boolean Status) {
        this.mCompleted = Status;
    }


}
