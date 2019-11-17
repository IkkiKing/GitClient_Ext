package com.ikkikingg.gitclient.GitRepo.Network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import static com.ikkikingg.gitclient.GitRepo.Network.Resource.Status.ERROR;
import static com.ikkikingg.gitclient.GitRepo.Network.Resource.Status.SUCCESS;
import static com.ikkikingg.gitclient.GitRepo.Network.Resource.Status.LOADING;


import com.ikkikingg.gitclient.GitRepo.Exception.AppException;

public class Resource<T> {
    public enum Status {
        SUCCESS, ERROR, LOADING
    }

    private final Status status;
    private final T data;
    private final AppException exception;

    private Resource(@NonNull Status status, @Nullable T data, @Nullable AppException exception) {
        this.status = status;
        this.data = data;
        this.exception = exception;
    }

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public AppException getException() {
        return exception;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(AppException exception, @Nullable T data) {
        return new Resource<>(ERROR, data, exception);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(LOADING, data, null);
    }
}
