package ada.osc.adateskekosti;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import javax.xml.transform.Result;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment {
    public ResultFragment() {
        // Required empty public constructor
    }


    @BindView(R.id.image_bmi_result)
    ImageView imageResult;
    @BindView(R.id.bmi_result)
    TextView bmiResult;
    @BindView(R.id.title_bmi_result)
    TextView titleResult;
    @BindView(R.id.text_bmi_result)
    TextView textResult;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_result, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    public void displayResult(double bmi) {
        if (String.valueOf(bmi).length() < 5) {
            bmiResult.setText(String.valueOf(bmi));
        } else {
            bmiResult.setText(String.valueOf(bmi).substring(0, 5));
        }
        if (bmi < 18.5) {
            imageResult.setImageResource(R.drawable.underweight);
            titleResult.setText(R.string.underweight_title);
            textResult.setText(R.string.underweight_description);
        } else if (bmi >= 18.5 && bmi < 25) {
            imageResult.setImageResource(R.drawable.healthy);
            titleResult.setText(R.string.healthy_title);
            textResult.setText(R.string.healthy_description);
        } else if (bmi >= 25 && bmi < 30) {
            imageResult.setImageResource(R.drawable.overweight);
            titleResult.setText(R.string.overweight_title);
            textResult.setText(R.string.overweight_description);
        } else if (bmi >= 30) {
            imageResult.setImageResource(R.drawable.obese);
            titleResult.setText(R.string.obese_title);
            textResult.setText(R.string.obese_description);
        }

    }

}
