package com.ikkikingg.gitclient.GitRepo.Repository;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import com.ikkikingg.gitclient.GitRepo.Database.GitRepoDao;
import com.ikkikingg.gitclient.GitRepo.Model.GitRepo;
import com.ikkikingg.gitclient.GitRepo.Network.ApiResponse;
import com.ikkikingg.gitclient.GitRepo.Network.GitHubRepoApi;
import com.ikkikingg.gitclient.GitRepo.Network.Resource;
import com.ikkikingg.gitclient.NetworkBoundResource.NetworkBoundResource;
import java.util.List;

import javax.inject.Inject;


public class GitRepoRepository {
    final private GitHubRepoApi gitHubRepoApi;
    final private GitRepoDao gitRepoDao;
    final private Context context;

    @Inject
    public GitRepoRepository(GitHubRepoApi gitHubRepoApi,
                             GitRepoDao gitRepoDao,
                             Context context) {
        this.gitHubRepoApi = gitHubRepoApi;
        this.gitRepoDao = gitRepoDao;
        this.context = context;
    }


    public LiveData<Resource<List<GitRepo>>> getAllRepos(final boolean networkRequest) {

        LiveData<Resource<List<GitRepo>>> liveData = new NetworkBoundResource<List<GitRepo>, List<GitRepo>>() {
            @Override
            protected void saveCallResult(@NonNull List<GitRepo> items) {
                GitRepo[] arr = new GitRepo[items.size()];
                items.toArray(arr);
                gitRepoDao.insertAll(arr);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<GitRepo> data) {
                return (data == null || data.isEmpty() || networkRequest);//let's always refresh to be up to date. data == null || data.isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<List<GitRepo>> loadFromDb() {
                return gitRepoDao.getAllRepos();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<GitRepo>>> createCall() {
                LiveData<ApiResponse<List<GitRepo>>> response = gitHubRepoApi.getAllReposLiveData();
                return response;
            }
        }.getAsLiveData();

        return liveData;
    }

}
