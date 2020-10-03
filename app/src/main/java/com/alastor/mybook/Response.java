package com.alastor.mybook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.alastor.mybook.Response.Status.ERROR;
import static com.alastor.mybook.Response.Status.LOADING;
import static com.alastor.mybook.Response.Status.SUCCESS;

public class Response<T> {
    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final Throwable error;

    private Response(Status status, @Nullable T data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> Response<T> loading() {
        return new Response<T>(LOADING, null, null);
    }

    public static <T> Response<T> success(@NonNull T data) {
        return new Response<>(SUCCESS, data, null);
    }

    public static <T> Response<T> error(@NonNull Throwable error) {
        return new Response<T>(ERROR, null, error);
    }

    public enum Status {
        LOADING,
        SUCCESS,
        ERROR
    }
}
