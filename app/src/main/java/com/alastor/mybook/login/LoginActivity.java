package com.alastor.mybook.login;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alastor.mybook.R;
import com.google.android.material.snackbar.Snackbar;

import static com.alastor.mybook.MainActivity.REQUEST_CODE_LOGIN_SUCCESS;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LoginViewModel loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.getLoginToken(getString(R.string.user_name), getString(R.string.password))
                .observe(this, stringResponse -> {
                    if (stringResponse == null) {
                        return;
                    }
                    switch (stringResponse.status) {
                        case LOADING:
                            break;
                        case SUCCESS:
                            setResult(REQUEST_CODE_LOGIN_SUCCESS);
                            finish();
                            break;
                        case ERROR:
                            break;
                    }
                });
    }
}
