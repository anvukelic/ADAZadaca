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
    private String id;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("content")
    private String description;
    @Expose
    @SerializedName("taskPriority")
    private int priority;
    @Expose
    @SerializedName("dueDate")
    private String date;
    @Expose
    @SerializedName("isCompleted")
    private boolean isCompleted;

    public Task() {
    }

    public Task(String title, String description, int priority, String date) {
        id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.date = date;
        isCompleted = false;
    }
    public Task(String id, String title, String description, int priority, String date){
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.date = date;
        isCompleted = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String mId) {
        this.id = mId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String mTitle) {
        this.title = mTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String mDescription) {
        this.description = mDescription;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int mPriority) {
        this.priority = mPriority;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String mDate) {
        this.date = mDate;
    }

    public boolean isDone() {
        return isCompleted;
    }

    public void setDone(boolean Status) {
        this.isCompleted = Status;
    }


}
