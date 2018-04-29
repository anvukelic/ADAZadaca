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

    public List<Task> getTasks(){
        return mDatabase.getTasks();
    }
}
