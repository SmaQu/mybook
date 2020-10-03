package com.alastor.mybook.repository.api.rest;

import com.alastor.mybook.repository.api.model.Book;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BookService {

    @GET("api/Books")
    Single<List<Book>> getBooks(@Header("Authorization") String header);

    @GET("api/Books/{id}")
    Single<Book> getBook(@Header("Authorization") String header, @Path("id") String id);

    @POST("api/Books")
    Single<String> createBook(@Header("Authorization") String header, Book book);

    @DELETE("api/Books/{id}")
    Single<String> removeBook(@Header("Authorization") String header, @Path("id") String id);
}
