package com.alastor.mybook.books.singledetailbook;

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
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class InDetailBookViewModel extends AndroidViewModel {

    private CompositeDisposable disposable = new CompositeDisposable();
    private BookRepository bookRepository = BookRepository.getInstance();
    private LoginRepository loginRepository = LoginRepository.getInstance();

    private MutableLiveData<Response<Book>> bookLiveData = new MutableLiveData<>();

    public InDetailBookViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Response<Book>> getBook(String id) {
        bookRepository
                .getBook(loginRepository.getNotExpiredToken(getApplication().getApplicationContext()), id)
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<Book>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                        bookLiveData.postValue(Response.loading());
                    }

                    @Override
                    public void onSuccess(Book books) {
                        bookLiveData.postValue(Response.success(books));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError: " + e);
                    }
                });

        return bookLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}