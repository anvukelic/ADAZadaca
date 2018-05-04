package ada.osc.taskie.model;

import java.util.Date;

/**
 * Created by avukelic on 28-Apr-18.
 */
public class Task {

    private String mTitle;
    private String mDescription;
    private int mPriority;
    private Date mDate;
    private boolean mDone;

    public Task(String mTitle, String mDescription, int mPriority, Date mDate) {
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mPriority = mPriority;
        this.mDate = mDate;
        mDone = false;
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
