package com.alastor.mybook.books.bookcreator;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alastor.mybook.Response;
import com.alastor.mybook.login.LoginRepository;
import com.alastor.mybook.repository.BookRepository;
import com.alastor.mybook.repository.api.model.Book;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CreateBookViewModel extends AndroidViewModel {

    private CompositeDisposable disposable = new CompositeDisposable();
    private BookRepository bookRepository = BookRepository.getInstance();
    private LoginRepository loginRepository = LoginRepository.getInstance();

    private MutableLiveData<Response<String>> createBookLiveData = new MutableLiveData<>();

    public CreateBookViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Response<String>> createBoot(Book book) {
        bookRepository
                .createBook(loginRepository.getNotExpiredToken(getApplication().getApplicationContext()), book)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                        createBookLiveData.setValue(Response.loading());
                    }

                    @Override
                    public void onSuccess(String s) {
                        createBookLiveData.setValue(Response.success(s));
                    }

                    @Override
                    public void onError(Throwable e) {
                        createBookLiveData.setValue(Response.error(e));
                    }
                });

        return createBookLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}