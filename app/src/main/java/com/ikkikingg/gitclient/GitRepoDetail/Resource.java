package com.ikkikingg.gitclient.GitRepoDetail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.ikkikingg.gitclient.GitRepoDetail.Resource.Status.ERROR;
import static com.ikkikingg.gitclient.GitRepoDetail.Resource.Status.LOADING;
import static com.ikkikingg.gitclient.GitRepoDetail.Resource.Status.SUCCESS;

public class Resource<T> {
    public enum Status {
        SUCCESS, ERROR, LOADING
    }

    private final Status status;
    private final T data;
    private final String message;

    private Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String message, @Nullable T data) {
        return new Resource<>(ERROR, data, message);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(LOADING, data, null);
    }
}
