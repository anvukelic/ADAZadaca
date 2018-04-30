package ada.osc.taskie.persistence;

import java.util.ArrayList;
import java.util.List;

import ada.osc.taskie.model.Task;

/**
 * Created by avukelic on 28-Apr-18.
 */
public class FakeDatabase {

    List<Task> mTasks = new ArrayList<>();

    public void insert(Task task){
        mTasks.add(task);
    }

    public void delete(Task task){
        mTasks.remove(task);
    }

    public void updateTaskPriorty(Task task) {
        Task selectedTask = mTasks.get(mTasks.indexOf(task));
        selectedTask.setPriority(selectedTask.getPriority()+1);
        if(selectedTask.getPriority()==3){
            selectedTask.setPriority(0);
        }
    }

    public List<Task> getTasks() {
        return new ArrayList<>(mTasks);
    }
}
