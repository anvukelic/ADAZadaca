package hr.ferit.bruno.exercise1.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.ferit.bruno.exercise1.R;
import hr.ferit.bruno.exercise1.TasksRepository;
import hr.ferit.bruno.exercise1.model.Task;
import hr.ferit.bruno.exercise1.persistance.FakeDatabase;

public class MainActivity extends AppCompatActivity {

    TasksRepository mRepository;

    @BindView(R.id.edittext_newtask_title)
    EditText taskTitle;
    @BindView(R.id.edittext_newtask_summary)
    EditText taskSummary;
    @BindView(R.id.edittext_newtask_importance)
    EditText taskImportance;

    @BindView(R.id.textview_newtask_display)
    TextView mDisplayText;

    @BindView(R.id.button_newtask_save)
    Button mSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mRepository = TasksRepository.getInstance();
    }

    @OnClick(R.id.button_newtask_save)
    public void storeTask(View view) {

        try {
            int importanceLevel = Integer.parseInt(taskImportance.getText().toString());
            Task task = new Task(importanceLevel,
                    taskTitle.getText().toString(),
                    taskSummary.getText().toString());
            mRepository.save(task);
            clearEditText();

            displayMessage();
        }catch (NumberFormatException e){
            Toast.makeText(this, "Task importance must be number", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearEditText() {
        taskImportance.setText(null);
        taskTitle.setText(null);
        taskSummary.setText(null);
    }

    private void displayMessage() {
        StringBuilder sb = new StringBuilder();
        for (Task task : mRepository.getTasks()) {
            sb.append(task);
        }
        mDisplayText.setText(sb.toString());

    }
}
