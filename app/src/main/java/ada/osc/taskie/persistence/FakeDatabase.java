package ada.osc.taskie.persistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ada.osc.taskie.model.Task;

/**
 * Created by avukelic on 28-Apr-18.
 */
public class FakeDatabase {

    public static final int SORT_ALL = 0;
    public static final int SORT_ASC = 1;
    public static final int SORT_DESC = 2;
    public static final int FILTER_ALL = 0;
    public static final int FILTER_DONE = 1;
    public static final int FILTER_NOT_DONE = 2;

    List<Task> mTasks = new ArrayList<>();

    public void insert(Task task) {
        mTasks.add(task);
    }

    public void delete(Task task) {
        mTasks.remove(task);
    }

    public void deleteAll() {
        mTasks.removeAll(mTasks);
    }

    public void changeTaskStatus(Task task){
        task.setDone(!task.isDone());
    }

    public void updateTaskPriorty(Task task) {
        task.updateTaskPriority(task);
    }

    public List<Task> getTasksSortedByStatus(int filterStatus) {
        List<Task> sortedList = new ArrayList<>();
        List<Task> filteredList = new ArrayList<>(mTasks);
        switch (filterStatus) {
            case 0:
                return mTasks;
            case 1:
                for (Task task : mTasks) {
                    if (!task.isDone()) {
                        sortedList.add(task);
                    }
                }
                break;
            case 2:
                for (Task task : mTasks) {
                    if (task.isDone()) {
                        sortedList.add(task);
                    }
                }
                break;
        }
        filteredList.removeAll(sortedList);
        return filteredList;
    }

    public List<Task> getTasks(int sortType, int filterStatus) {
        List<Task> sortedList = new ArrayList<>(getTasksSortedByStatus(filterStatus));
        switch (sortType) {
            case SORT_ALL:
                return new ArrayList<>(getTasksSortedByStatus(filterStatus));
            case SORT_ASC:
                Collections.sort(sortedList, new Comparator<Task>() {
                    @Override
                    public int compare(Task t1, Task t2) {
                        return Integer.compare(t1.getPriority(), t2.getPriority());
                    }
                });
                break;
            case SORT_DESC:
                Collections.sort(sortedList, new Comparator<Task>() {
                    @Override
                    public int compare(Task t1, Task t2) {
                        return Integer.compare(t2.getPriority(), t1.getPriority());
                    }
                });
                break;
        }
        return sortedList;

    }
}
