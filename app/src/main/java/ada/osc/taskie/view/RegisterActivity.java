package ada.osc.taskie.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import ada.osc.taskie.R;
import ada.osc.taskie.model.RegistrationToken;
import ada.osc.taskie.networking.ApiService;
import ada.osc.taskie.networking.RetrofitUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.edittext_register_username)
    EditText mUsername;
    @BindView(R.id.edittext_register_user_email)
    EditText mUserEmail;
    @BindView(R.id.edittext_register_user_password)
    EditText mUserPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_register_login)
    void onLoginButtonClick(){
        startLoginActivity();
    }

    @OnClick(R.id.button_register_register)
    void onRegisterButtonClick(){
        registerUser();
    }

    private void registerUser() {
        Retrofit retrofit = RetrofitUtil.createRetrofit();
        ApiService apiService = retrofit.create(ApiService.class);

        RegistrationToken registrationToken = new RegistrationToken();
        registrationToken.mEmail = mUserEmail.getText().toString();
        registrationToken.mUserName = mUsername.getText().toString();
        registrationToken.mPassword = mUserPassword.getText().toString();

        Call registerCall = apiService.registerUser(registrationToken);
        registerCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                System.out.println(response);
                startLoginActivity();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.fillInStackTrace();
            }
        });
    }

    private void startLoginActivity() {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
    }
}