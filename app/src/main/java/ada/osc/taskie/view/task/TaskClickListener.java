package ada.osc.taskie.view.task;

import ada.osc.taskie.model.Task;

/**
 * Created by avukelic on 30-Apr-18.
 */
public interface TaskClickListener {
    void onClick(Task task);
    void onPriorityChangeClick(Task task);
    void onStatusSwitchChange(Task task);
}
