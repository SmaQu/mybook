package com.alastor.mybook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alastor.mybook.books.BooksFragment;
import com.alastor.mybook.login.LoginActivity;
import com.alastor.mybook.login.LoginRepository;
import com.alastor.mybook.login.LoginSessionController;
import com.alastor.mybook.repository.BookRepository;
import com.alastor.mybook.repository.api.model.Book;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements LoginSessionController.SessionListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int REQUEST_CODE_LOGIN_SUCCESS = 1410;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LoginSessionController loginSessionController = new LoginSessionController(this, this);
        getLifecycle().addObserver(loginSessionController);
        if (savedInstanceState == null && !loginSessionController.isSessionExpired()) {
            addBooksFragment();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final boolean mainFragmentExist = FragmentAdministrator
                .isFragmentPresent(getSupportFragmentManager(),
                        R.id.fragment_container,
                        BooksFragment.class.getSimpleName());
        if (!mainFragmentExist) {
            addBooksFragment();
        }
    }

    @Override
    public void onSessionInactive() {
        startActivityForResult(new Intent(this, LoginActivity.class), REQUEST_CODE_LOGIN_SUCCESS);
    }

    private void addBooksFragment() {
        FragmentAdministrator.addFragment(getSupportFragmentManager(),
                R.id.fragment_container,
                BooksFragment.newInstance());
    }
}