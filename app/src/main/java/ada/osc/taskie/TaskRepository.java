package ada.osc.taskie;

import java.util.List;

import ada.osc.taskie.model.Task;
import ada.osc.taskie.persistence.FakeDatabase;

/**
 * Created by avukelic on 28-Apr-18.
 */
public class TaskRepository {

    private static TaskRepository sRepository = null;

    private FakeDatabase mDatabase;

    private TaskRepository(){
        mDatabase = new FakeDatabase();
    }

    public static TaskRepository getInstance(){
        if(sRepository == null){
            sRepository = new TaskRepository();
        }
        return sRepository;
    }

    public void save(Task task){
        mDatabase.insert(task);
    }

    public void delete(Task task){
        mDatabase.delete(task);
    }

    public void deleteAll(){mDatabase.deleteAll();}

    public void changeTaskStatus(Task task){
        mDatabase.changeTaskStatus(task);
    }

    public void updateTaskPriority(Task task){mDatabase.updateTaskPriorty(task);}

    public List<Task> getTasks(int sortType, int filterStatus){
        return mDatabase.getTasks(sortType, filterStatus);
    }

}
