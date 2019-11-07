package com.ikkikingg.gitclient.GitRepoDetail.Network;

import androidx.lifecycle.LiveData;
import com.ikkikingg.gitclient.GitRepo.Network.ApiResponse;
import com.ikkikingg.gitclient.GitRepoDetail.Model.GitRepoDetail;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitRepoDetailApi {

    @GET("/repos/{owner}/{repo}")
    Call<GitRepoDetail> getRepoDetail(@Path("owner") String owner,
                                      @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}")
    LiveData<ApiResponse<GitRepoDetail>> getRepoDetailLiveData(@Path("owner") String owner,
                                                               @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/commits/{sha}")
    ApiResponse<List<Commits>> getCommits(@Path("owner") String owner,
                                          @Path("repo") String repo,
                                          @Path("sha") String sha);
}
