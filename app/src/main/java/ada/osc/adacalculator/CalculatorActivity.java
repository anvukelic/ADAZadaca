package ada.osc.adacalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalculatorActivity extends AppCompatActivity {

    @BindView(R.id.firstNumber)
    EditText firstNumberInput;
    @BindView(R.id.secondNumber)
    EditText secondNumberInput;

    @BindView(R.id.btnAddition)
    Button btnAddition;
    @BindView(R.id.btnSubtraction)
    Button btnSubtraction;
    @BindView(R.id.btnMultiplication)
    Button btnMultiplication;
    @BindView(R.id.btnDivision)
    Button btnDivision;
    @BindView(R.id.btnReset)
    Button btnReset;

    @BindView(R.id.result)
    TextView displayResult;

    private int firstNumber, secondNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btnAddition)
    public void doAddition() { doAction(R.id.btnAddition); }

    @OnClick(R.id.btnSubtraction)
    public void doSubtraction() { doAction(R.id.btnSubtraction); }

    @OnClick(R.id.btnMultiplication)
    public void doMultiplication() { doAction(R.id.btnMultiplication); }

    @OnClick(R.id.btnDivision)
    public void doDivision() { doAction(R.id.btnDivision); }

    @OnClick(R.id.btnReset)
    public void resetFields() {
        firstNumberInput.setText(null);
        secondNumberInput.setText(null);
        displayResult.setText(null);
    }

    private boolean doAction(int btnId) {
        try {
            firstNumber = Integer.parseInt(firstNumberInput.getText().toString());
            secondNumber = Integer.parseInt(secondNumberInput.getText().toString());
            switch (btnId) {
                case R.id.btnAddition:
                    displayResult.setTextColor(getResources().getColor(R.color.colorAddition));
                    displayResult.setText(String.valueOf(firstNumber + secondNumber));
                    return true;
                case R.id.btnSubtraction:
                    displayResult.setTextColor(getResources().getColor(R.color.colorSubtraction));
                    displayResult.setText(String.valueOf(firstNumber - secondNumber));
                    return true;
                case R.id.btnMultiplication:
                    displayResult.setTextColor(getResources().getColor(R.color.colorMultiplication));
                    displayResult.setText(String.valueOf(firstNumber * secondNumber));
                    return true;
                case R.id.btnDivision:
                    displayResult.setTextColor(getResources().getColor(R.color.colorAddition));
                    displayResult.setText(String.valueOf((float) firstNumber / (float) secondNumber));
                    return true;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.empty_number, Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
