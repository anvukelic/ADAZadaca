package ada.osc.taskie.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by avukelic on 28-Apr-18.
 */
@DatabaseTable(tableName = "tasks")
public class Task {

    @DatabaseField(columnName = "id", id = true)
    private String mId;
    @DatabaseField(columnName = "title")
    private String mTitle;
    @DatabaseField(columnName = "description")
    private String mDescription;
    @DatabaseField(columnName = "priority")
    private int mPriority;
    @DatabaseField(columnName = "date")
    private Date mDate;
    @DatabaseField(columnName = "status")
    private boolean mDone;

    public Task() {
    }

    public Task(String mTitle, String mDescription, int mPriority, Date mDate) {
        mId = UUID.randomUUID().toString();
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mPriority = mPriority;
        this.mDate = mDate;
        mDone = false;
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

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean isDone() {
        return mDone;
    }

    public void setDone(boolean Status) {
        this.mDone = Status;
    }

    public void updateTaskPriority(Task task) {
        switch (task.getPriority()) {
            case 0:
                task.setPriority(1);
                break;
            case 1:
                task.setPriority(2);
                break;
            case 2:
                task.setPriority(0);
                break;
        }
    }


}
