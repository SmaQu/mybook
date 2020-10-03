package com.alastor.mybook.repository;

import android.util.Log;

import com.alastor.mybook.repository.api.model.Book;
import com.alastor.mybook.repository.api.rest.BookRDS;
import com.alastor.mybook.repository.api.rest.BookServiceGenerator;

import java.util.List;

import io.reactivex.Single;

public class BookRepository {

    private static BookRepository repositoryInstance;
    private static BookRDS bookRDS;

    private BookRepository() {
    }

    //Only to hide apiUrl
    public static void init(String apiUrl) {
        getInstance();
        bookRDS = new BookRDS(new BookServiceGenerator().provideRetrofit(apiUrl));
    }

    public static BookRepository getInstance() {
        if (repositoryInstance == null) {
            synchronized (BookRepository.class) {
                if (repositoryInstance == null) {
                    repositoryInstance = new BookRepository();
                }
            }
        }
        return repositoryInstance;
    }

    public Single<String> loginUser(String username, String password) {
        return bookRDS.login(username, password);
    }

    public Single<List<Book>> getBooks(String header) {
        return bookRDS.getBooks(header);
    }

    public Single<Book> getBook(String header, String id) {
        return bookRDS.getBook(header, id);
    }

    public Single<String> createBook(String header, Book book) {
        return bookRDS.createBook(header, book);
    }

    public Single<String> removeBook(String header, String id) {
        return bookRDS.removeBook(header, id);
    }
}
