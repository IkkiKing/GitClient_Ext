package com.ikkikingg.gitclient.GitRepoDetail.DI.Module;

import com.ikkikingg.gitclient.GitRepoDetail.Network.GitRepoDetailApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class GitRepoDetailApiModule {
    @Provides
    @Singleton
    public GitRepoDetailApi providesGitRepoDetailApi(Retrofit retrofit) {
        return retrofit.create(GitRepoDetailApi.class);
    }
}
