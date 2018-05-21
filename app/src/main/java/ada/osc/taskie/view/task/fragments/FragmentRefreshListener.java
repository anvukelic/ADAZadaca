package ada.osc.taskie.view.task.fragments;

import ada.osc.taskie.model.Task;

/**
 * Created by avukelic on 18-May-18.
 */
public interface FragmentRefreshListener {
    void onCreateTask(Task task);
    void onTaskUpdate(Task task);
}
