package com.alastor.mybook.repository.api.model;

import androidx.annotation.StringRes;

import com.google.gson.annotations.SerializedName;

public class Book {

    public Book(String title, String description, String coverUrl) {
        this.title = title;
        this.description = description;
        this.coverUrl = coverUrl;
    }

    @SerializedName("Id")
    String id;

    @SerializedName("Title")
    String title;

    @SerializedName("Description")
    String description;

    @SerializedName("CoverUrl")
    String coverUrl;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCoverUrl() {
        return coverUrl;
    }


}
