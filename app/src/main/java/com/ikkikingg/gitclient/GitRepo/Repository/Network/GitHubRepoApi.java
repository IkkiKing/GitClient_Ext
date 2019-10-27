package com.ikkikingg.gitclient.GitRepo.Repository.Network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubRepoApi {

    @GET("/users/{user}/repos")
    Call<List<Repo>> getRepoForUser(@Path("user") String user);

    @GET("/repositories")
    Call<List<Repo>> getAllRepos();

}
