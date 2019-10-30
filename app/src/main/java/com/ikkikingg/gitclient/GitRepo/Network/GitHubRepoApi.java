package com.ikkikingg.gitclient.GitRepo.Network;

import androidx.lifecycle.LiveData;

import com.ikkikingg.gitclient.GitRepo.Model.GitRepo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface GitHubRepoApi {

    @GET("/users/{user}/repos")
    public Call<List<GitRepo>> getRepoForUser(@Path("user") String user);

    @GET("/repositories")
    public  Call<List<GitRepo>> getAllRepos();

    @GET("/repositories")
    public LiveData<ApiResponse<List<GitRepo>>> getAllReposLiveData();


}
