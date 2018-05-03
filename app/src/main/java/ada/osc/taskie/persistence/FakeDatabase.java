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

    public static final int FILTER_ALL = 0;
    public static final int FILTER_ASC = 1;
    public static final int FILTER_DESC = 2;

    List<Task> mTasks = new ArrayList<>();

    public void insert(Task task) {
        mTasks.add(task);
    }

    public void delete(Task task) {
        mTasks.remove(task);
    }

    public void deleteAll(){mTasks.removeAll(mTasks);}

    public void updateTaskPriorty(Task task) {
        task.updateTaskPriority(task);
    }

    public List<Task> getTasks(int sortType) {
        List<Task> sortedList = new ArrayList<>();
        sortedList.addAll(mTasks);
        switch (sortType) {
            case FILTER_ALL:
                return new ArrayList<>(mTasks);
            case FILTER_ASC:
                Collections.sort(sortedList, new Comparator<Task>() {
                    @Override
                    public int compare(Task t1, Task t2) {
                        return Integer.compare(t1.getPriority(), t2.getPriority());
                    }
                });
                break;
            case FILTER_DESC:
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
