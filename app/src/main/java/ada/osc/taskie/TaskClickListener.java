package ada.osc.taskie;

import android.view.View;

import ada.osc.taskie.model.Task;

/**
 * Created by avukelic on 30-Apr-18.
 */
public interface TaskClickListener {
    void onClick(Task task);
    boolean onLongClick(Task task);
    void onPriorityChangeClick(Task task);
    void onStatusSwitchChange(Task task);
}
