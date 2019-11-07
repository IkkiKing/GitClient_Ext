package com.ikkikingg.gitclient.GitRepoDetail.Network;

import androidx.room.Embedded;

import com.google.gson.annotations.SerializedName;


public class Commit {

    @Embedded
    private Author author;
    @SerializedName("message")
    private String message;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
