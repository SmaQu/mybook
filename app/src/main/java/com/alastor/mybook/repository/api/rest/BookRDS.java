package com.alastor.mybook.repository.api.rest;

import android.util.Base64;

import com.alastor.mybook.repository.api.model.Book;

import java.util.List;

import io.reactivex.Single;

public class BookRDS {

    private final BookService bookService;

    public BookRDS(BookService bookService) {
        this.bookService = bookService;
    }

    public Single<String> login(String username, String password) {
        return Single.just(dummyImplementationOfTokenResponse(username, password));
    }

    public Single<List<Book>> getBooks(String header) {
        return bookService.getBooks(header);
    }

    public Single<Book> getBook(String header,String id) {
        return bookService.getBook(header,id);
    }

    public Single<String> createBook(String header,Book book) {
        return bookService.createBook(header, book);
    }

    public Single<String> removeBook(String header,String id) {
        return bookService.removeBook(header, id);
    }

    private static String dummyImplementationOfTokenResponse(String username, String password) {
        String credentials = username + ":" + password;
        return "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }
}
