package com.alastor.mybook;

import android.app.Application;
import android.util.Log;

import com.alastor.mybook.repository.BookRepository;

public class MyBookApplication extends Application {

    @Override
    public void onCreate() {
        BookRepository.init(getString(R.string.api_base_url));
        super.onCreate();
    }
}
