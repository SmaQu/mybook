package com.alastor.mybook.login;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alastor.mybook.Response;
import com.alastor.mybook.repository.BookRepository;
import com.alastor.mybook.repository.api.rest.LoginRepository;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends AndroidViewModel {

    private CompositeDisposable disposable = new CompositeDisposable();
    private BookRepository bookRepository = BookRepository.getInstance();
    private LoginRepository loginRepository = LoginRepository.getInstance();

    private MutableLiveData<Response<String>> loginTokenLiveData = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Response<String>> getLoginToken(String username, String password) {
        final String notExpiredToken = loginRepository
                .getNotExpiredToken(getApplication().getApplicationContext());
        if (notExpiredToken.isEmpty()) {
            bookRepository.loginUser(username, password)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new SingleObserver<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                            loginTokenLiveData.postValue(Response.loading());
                        }

                        @Override
                        public void onSuccess(String s) {
                            loginRepository.setToken(getApplication().getApplicationContext(), s);
                            loginTokenLiveData.postValue(Response.success(s));
                        }

                        @Override
                        public void onError(Throwable e) {
                            loginTokenLiveData.postValue(Response.error(e));
                        }
                    });
        } else {
            loginTokenLiveData.postValue(Response.success(notExpiredToken));
        }

        return loginTokenLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
