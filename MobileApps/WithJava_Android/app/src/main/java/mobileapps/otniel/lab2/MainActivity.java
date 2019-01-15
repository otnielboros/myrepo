package mobileapps.otniel.lab2;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import mobileapps.otniel.lab2.api.TokenRequestBody;
import mobileapps.otniel.lab2.api.TaskResource;
import mobileapps.otniel.lab2.persistance.token.Token;
import mobileapps.otniel.lab2.persistance.token.TokenDatabase;
import mobileapps.otniel.lab2.persistance.token.TokenRepository;
import mobileapps.otniel.lab2.viewmodel.LoginViewModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private EditText usernameText;
    private EditText passwordText;
    private TokenRepository tokenRepository;
    private Button loginButton;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Username = "username";
    public static final String Password = "password";
    SharedPreferences sharedpreferences;
    Intent in;

    private void startActivity(){
        in = new Intent(MainActivity.this,TaskActivity.class);
        startActivity(in);
    }

    public void login(final String username, final String password)
    {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TaskResource.BASE_URL).addConverterFactory(GsonConverterFactory.create())
                    .build();

            TaskResource api = retrofit.create(TaskResource.class);
            Call<Token> tokenCall = api.getToken("application/x-www-form-urlencoded", "Basic dGVzdGp3dGNsaWVudGlkOlhZN2ttem9OemwxMDA",
                    "outlook.allow-unsafe-html", "password", username, password);

            tokenCall.enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    String token = response.body().getAccess_token();
                    tokenRepository.update(token);
                    Toast.makeText(getBaseContext(), "Successfully Logged In!", Toast.LENGTH_LONG).show();
                    Log.i("Successful_Login", "Login was successful");
                    startActivity();
                }

                @Override
                public void onFailure(Call<Token> call, Throwable t) {
                    Log.d("Token;", "EROARE, ori nu e serverul pornit, ori nu esti pe reteaua ta!");
                    Toast.makeText(getBaseContext(), "Invalid Login!", Toast.LENGTH_SHORT).show();
                    Log.i("Unsuccessful_Login", "Login was not successful");
                   // login(username,password);
                }
            });

    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        usernameText = (EditText) findViewById(R.id.username);
        passwordText = (EditText) findViewById(R.id.password);


        loginButton = (Button) findViewById(R.id.loginBtn);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModel.Factory(getApplicationContext())).get(LoginViewModel.class);


        tokenRepository=TokenRepository.getInstance(TokenDatabase.getAppDatabase(getApplicationContext()).tokenDao());
        if(tokenRepository.getToken()==null){
            tokenRepository.insert(new Token(1,""));
        }
        Token token=tokenRepository.getToken();
        if(!token.getAccess_token().equals("")){
            startActivity();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(usernameText.getText().toString(), passwordText.getText().toString());
            }
        });
    }
}
