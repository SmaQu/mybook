package com.alastor.mybook.books;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alastor.mybook.Response;
import com.alastor.mybook.login.LoginRepository;
import com.alastor.mybook.repository.BookRepository;
import com.alastor.mybook.repository.api.model.Book;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BooksViewModel extends AndroidViewModel {

    private CompositeDisposable disposable = new CompositeDisposable();
    private BookRepository bookRepository = BookRepository.getInstance();
    private LoginRepository loginRepository = LoginRepository.getInstance();

    private MutableLiveData<Response<List<Book>>> booksLiveData = new MutableLiveData<>();
    private MutableLiveData<Response<String>> removeBookLiveData = new MutableLiveData<>();

    public BooksViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Response<List<Book>>> getBooks() {
        bookRepository
                .getBooks(loginRepository.getNotExpiredToken(getApplication().getApplicationContext()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Book>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                        booksLiveData.setValue(Response.loading());
                    }

                    @Override
                    public void onSuccess(List<Book> books) {
                        booksLiveData.setValue(Response.success(books));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError: " + e);
                    }
                });

        return booksLiveData;
    }

    public LiveData<Response<String>> removeBook(String id) {
        bookRepository
                .removeBook(loginRepository.getNotExpiredToken(getApplication().getApplicationContext()), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                        removeBookLiveData.setValue(Response.loading());
                    }

                    @Override
                    public void onSuccess(String s) {
                        removeBookLiveData.setValue(Response.success(s));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError: " + e);
                    }
                });
        return removeBookLiveData;

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}