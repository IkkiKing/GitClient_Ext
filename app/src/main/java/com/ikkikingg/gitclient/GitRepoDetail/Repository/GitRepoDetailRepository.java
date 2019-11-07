package com.ikkikingg.gitclient.GitRepoDetail.Repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.ikkikingg.gitclient.GitRepo.Network.ApiResponse;
import com.ikkikingg.gitclient.GitRepo.Network.Resource;
import com.ikkikingg.gitclient.GitRepoDetail.Database.GitRepoDetailDao;
import com.ikkikingg.gitclient.GitRepoDetail.Model.GitRepoDetail;
import com.ikkikingg.gitclient.GitRepoDetail.Network.GitRepoDetailApi;
import com.ikkikingg.gitclient.NetworkBoundResource.NetworkBoundResource;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GitRepoDetailRepository {
    final private GitRepoDetailApi gitRepoDetailApi;
    final private GitRepoDetailDao gitRepoDetailDao;
    final private Context context;

    @Inject
    public GitRepoDetailRepository(GitRepoDetailApi gitRepoDetailApi,
                                   GitRepoDetailDao gitRepoDetailDao,
                                   Context context) {
        this.gitRepoDetailApi = gitRepoDetailApi;
        this.gitRepoDetailDao = gitRepoDetailDao;
        this.context = context;
    }

    public LiveData<Resource<GitRepoDetail>> getRepoDetails(final boolean networkRequest,
                                                            final String owner,
                                                            final String repoName) {

        LiveData<Resource<GitRepoDetail>> liveData = new NetworkBoundResource<GitRepoDetail, GitRepoDetail>() {
            @Override
            protected void saveCallResult(@NonNull GitRepoDetail item) {
                gitRepoDetailDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable GitRepoDetail data) {
                return (data == null || networkRequest);//let's always refresh to be up to date. data == null || data.isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<GitRepoDetail> loadFromDb() {
                return gitRepoDetailDao.getGitRepoDetail(owner, repoName);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<GitRepoDetail>> createCall() {
                Log.d("TAG", owner + "  " + repoName);

                LiveData<ApiResponse<GitRepoDetail>> response = gitRepoDetailApi.getRepoDetailLiveData(owner, repoName);

                return response;
            }
        }.getAsLiveData();

        return liveData;
    }

    /*private void createCall(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }*/

}
