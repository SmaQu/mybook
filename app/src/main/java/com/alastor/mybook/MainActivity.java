package com.alastor.mybook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

    private CompositeDisposable disposable = new CompositeDisposable();
    private LoginSessionController loginSessionController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginSessionController = new LoginSessionController(this, this);
        getLifecycle().addObserver(loginSessionController);
        if (!loginSessionController.isSessionExpired()) {
            requestBooks();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        requestBooks();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

    @Override
    public void onSessionInactive() {
        startActivityForResult(new Intent(this, LoginActivity.class), REQUEST_CODE_LOGIN_SUCCESS);
    }

    private void requestBooks() {
        BookRepository bookRepository = BookRepository.getInstance();
        bookRepository
                .getBooks(LoginRepository.getInstance().getNotExpiredToken(getApplicationContext()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Book>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<Book> books) {
                        for (Book book : books) {
                            Log.e(TAG, "onSuccess: " + book.getTitle());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                    }
                });
    }
}