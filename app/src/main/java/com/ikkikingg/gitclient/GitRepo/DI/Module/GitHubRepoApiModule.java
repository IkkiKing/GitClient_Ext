package com.ikkikingg.gitclient.GitRepo.DI.Module;

import com.ikkikingg.gitclient.GitRepo.Network.GitHubRepoApi;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class GitHubRepoApiModule {
    @Provides
    @Singleton
    public GitHubRepoApi providesGitHubRepoApi(Retrofit retrofit) {
        return retrofit.create(GitHubRepoApi.class);
    }
}
