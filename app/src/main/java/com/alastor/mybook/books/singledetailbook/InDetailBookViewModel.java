package com.alastor.mybook.books.singledetailbook;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alastor.mybook.Response;
import com.alastor.mybook.login.LoginRepository;
import com.alastor.mybook.repository.BookRepository;
import com.alastor.mybook.repository.api.model.Book;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class InDetailBookViewModel extends AndroidViewModel {

    private CompositeDisposable disposable = new CompositeDisposable();
    private BookRepository bookRepository = BookRepository.getInstance();
    private LoginRepository loginRepository = LoginRepository.getInstance();

    private MutableLiveData<Response<Book>> bookLiveData = new MutableLiveData<>();
    private MutableLiveData<Response<Drawable>> bookCoverLiveData = new MutableLiveData<>();

    public InDetailBookViewModel(@NonNull Application application) {
        super(application);
    }

    public void requestBook(String id) {
        getBook(id);
    }

    public LiveData<Response<Book>> getBook(String id) {
        bookRepository
                .getBook(loginRepository.getNotExpiredToken(getApplication().getApplicationContext()), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Book>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                        bookLiveData.setValue(Response.loading());
                    }

                    @Override
                    public void onSuccess(Book books) {
                        getCover(books.getCoverUrl());
                        bookLiveData.postValue(Response.success(books));
                    }

                    @Override
                    public void onError(Throwable e) {
                        bookLiveData.setValue(Response.error(e));
                    }
                });

        return bookLiveData;
    }

    public LiveData<Response<Drawable>> getCoverLiveData() {
        return bookCoverLiveData;
    }

    public LiveData<Response<Drawable>> getCover(String url) {
        bookCoverLiveData.postValue(Response.loading());
        Glide.with(getApplication())
                .load(url)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        bookCoverLiveData.setValue(Response.success(resource));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        bookCoverLiveData.setValue(Response.error(null));
                    }
                });

        return bookCoverLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}