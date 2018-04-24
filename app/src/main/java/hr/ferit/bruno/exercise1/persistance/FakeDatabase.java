package hr.ferit.bruno.exercise1.persistance;

import java.util.ArrayList;
import java.util.List;

import hr.ferit.bruno.exercise1.model.Task;

public class FakeDatabase {

	private List<Task> mTasks;

	public FakeDatabase() {
		mTasks = new ArrayList<>();
	}

	public void save(Task task) {
		mTasks.add(task);
	}

	public List<Task> getTasks() {
		return mTasks;
	}
}
