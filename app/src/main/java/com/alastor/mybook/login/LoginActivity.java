package com.alastor.mybook.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alastor.mybook.MainActivity;
import com.alastor.mybook.R;

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
                            startActivity(new Intent(this, MainActivity.class));
                            break;
                        case ERROR:
                            break;
                    }
                });
    }
}
