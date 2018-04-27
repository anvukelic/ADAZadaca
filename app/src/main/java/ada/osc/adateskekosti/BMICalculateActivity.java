package ada.osc.adateskekosti;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BMICalculateActivity extends AppCompatActivity {

    @BindView(R.id.input_height)
    EditText userHeight;
    @BindView(R.id.input_weight)
    EditText userWeight;

    @BindView(R.id.calculate_bmi)
    Button calculate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_calculate);

        ButterKnife.bind(this);


    }

    @OnClick(R.id.calculate_bmi)
    public void calculateBmi() {
        if (userHeight.getText().toString().trim().length() == 0 || userHeight.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please insert height", Toast.LENGTH_SHORT).show();
        } else if (userWeight.getText().toString().trim().length() == 0 || userWeight.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please insert weight", Toast.LENGTH_SHORT).show();
        } else if (Double.parseDouble(userHeight.getText().toString()) < 1.3 || Double.parseDouble(userHeight.getText().toString()) > 2.5) {
            Toast.makeText(this, "Height must be between 130 m's and 2.5 m's", Toast.LENGTH_SHORT).show();
        } else if (Double.parseDouble(userWeight.getText().toString()) < 20 || Double.parseDouble(userWeight.getText().toString()) > 350) {
            Toast.makeText(this, "Weight must be between 20 kg's and 350 kg's", Toast.LENGTH_SHORT).show();
        } else {
            ResultFragment resultFragment = (ResultFragment) getSupportFragmentManager().findFragmentById(R.id.bmi_result_fragment);
            resultFragment.displayResult((Double.parseDouble(userWeight.getText().toString())) / Math.pow(Double.parseDouble(userHeight.getText().toString()), 2));
        }
    }
    //Remove keyboard and selection from edittext on layout click
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

}


