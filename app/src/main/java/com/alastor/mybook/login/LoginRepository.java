package com.alastor.mybook.login;

import android.content.Context;

import androidx.lifecycle.LifecycleObserver;

import com.alastor.mybook.repository.BookRepository;

public class LoginRepository {

    private static LoginRepository repositoryInstance;

    private LoginRepository() {
    }

    public static LoginRepository getInstance() {
        if (repositoryInstance == null) {
            synchronized (BookRepository.class) {
                if (repositoryInstance == null) {
                    repositoryInstance = new LoginRepository();
                }
            }
        }
        return repositoryInstance;
    }

    private static final String TOKEN_FOLDER = "token_folder";
    private static final String KEY_TOKEN = "key_token";

    public void setToken(Context context, String token) {
        context.getSharedPreferences(TOKEN_FOLDER, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_TOKEN, token)
                .apply();
    }

    public String getNotExpiredToken(Context context) {
        //Implementation that check is token expired
        return context.getSharedPreferences(TOKEN_FOLDER, Context.MODE_PRIVATE)
                .getString(KEY_TOKEN,"");
    }
}
