package com.alastor.mybook.login;

import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class LoginSessionController implements LifecycleObserver {

    private SessionListener sessionListener;
    private Context context;

    public LoginSessionController(SessionListener sessionListener, Context context) {
        this.sessionListener = sessionListener;
        this.context = context;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public boolean isSessionExpired() {
        final boolean isSessionExpired =
                isTokeExpired(LoginRepository.getInstance().getNotExpiredToken(context));
        if (isSessionExpired && sessionListener != null) {
            sessionListener.onSessionInactive();
        }
        return isSessionExpired;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void clear() {
        sessionListener = null;
        context = null;
    }

    public interface SessionListener {
        void onSessionInactive();
    }

    //Mocked checker
    private boolean isTokeExpired(String token) {
        return false;
    }
}
