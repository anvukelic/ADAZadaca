package ada.osc.taskie.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by avukelic on 28-Apr-18.
 */

public class Task implements Serializable {

    public static final String TASK_TABLE = "tasks";
    public static final String TASK_ID = "id";
    public static final String TASK_TITLE = "title";
    public static final String TASK_DESCRIPTION = "description";
    public static final String TASK_PRIORITY = "priority";
    public static final String TASK_DATE = "date";
    public static final String TASK_STATUS = "status";


    @Expose
    @SerializedName("id")
    private String mId;
    @Expose
    @SerializedName("title")
    private String mTitle;
    @Expose
    @SerializedName("content")
    private String mDescription;
    @Expose
    @SerializedName("taskPriority")
    private int mPriority;
    @Expose
    @SerializedName("dueDate")
    private String mDate;
    @Expose
    @SerializedName("isCompleted")
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
    public Task(String mId, String mTitle, String mDescription, int mPriority, String mDate){
        this.mId = mId;
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
