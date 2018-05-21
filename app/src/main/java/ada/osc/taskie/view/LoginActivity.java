package ada.osc.taskie.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import ada.osc.taskie.R;
import ada.osc.taskie.model.LoginResponse;
import ada.osc.taskie.model.RegistrationToken;
import ada.osc.taskie.networking.ApiService;
import ada.osc.taskie.networking.RetrofitUtil;
import ada.osc.taskie.util.SharedPrefsUtil;
import ada.osc.taskie.view.task.TaskActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.edittext_login_user_email)
    EditText mUserEmail;
    @BindView(R.id.edittext_login_user_password)
    EditText mUserPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        isFirstRun();
    }

    @OnClick(R.id.button_login_login)
    void onLoginButtonClick(){
        loginUser();
    }

    @OnClick(R.id.button_login_register)
    void onRegisterButtonClick(){
        startRegisterActivity();
    }

    private void loginUser() {
        Retrofit retrofit = RetrofitUtil.createRetrofit();
        ApiService apiService = retrofit.create(ApiService.class);
        final RegistrationToken registrationToken = new RegistrationToken();
        registrationToken.mEmail = mUserEmail.getText().toString();
        registrationToken.mPassword = mUserPassword.getText().toString();
        System.out.println(mUserPassword.getText().toString());

        final Call<LoginResponse> loginCall = apiService.loginUser(registrationToken);
        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()){
                    LoginResponse loginResponse = response.body();
                    SharedPrefsUtil.storePreferencesField(LoginActivity.this, SharedPrefsUtil.TOKEN, loginResponse.mToken);

                    startTaskActivity();
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,"Username or password invalid", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
            }
        });
    }
    private void startTaskActivity() {
        Intent intent = new Intent();
        intent.setClass(this, TaskActivity.class);
        startActivity(intent);
    }
    private void startRegisterActivity(){
        startActivity(new Intent(this,RegisterActivity.class));
    }

    private void isFirstRun() {
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        if (isFirstRun) {
            //show sign up activity
            startActivity(new Intent(this, RegisterActivity.class));
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).apply();
    }

}
