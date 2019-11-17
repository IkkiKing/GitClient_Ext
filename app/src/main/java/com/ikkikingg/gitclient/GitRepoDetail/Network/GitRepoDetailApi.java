package com.ikkikingg.gitclient.GitRepoDetail.Network;

import androidx.lifecycle.LiveData;
import com.ikkikingg.gitclient.GitRepo.Network.ApiResponse;
import com.ikkikingg.gitclient.GitRepoDetail.Model.GitRepo;
import com.ikkikingg.gitclient.GitRepoDetail.Model.GitRepoDetail;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitRepoDetailApi {



    @GET("/repos/{owner}/{repo}")
    Flowable<GitRepoDetail> getRepoDetail(@Path("owner") String owner,
                                            @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/commits")
    Flowable<List<Commits>> getCommits(@Path("owner") String owner,
                                         @Path("repo") String repo);
}
