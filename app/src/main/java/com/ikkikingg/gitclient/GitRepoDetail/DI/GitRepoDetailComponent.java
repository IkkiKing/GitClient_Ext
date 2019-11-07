package com.ikkikingg.gitclient.GitRepoDetail.DI;


import android.content.Context;

import com.ikkikingg.gitclient.GitRepoDetail.DI.Module.AppModule;
import com.ikkikingg.gitclient.GitRepoDetail.DI.Module.DaoModule;
import com.ikkikingg.gitclient.GitRepoDetail.DI.Module.GitRepoDetailApiModule;
import com.ikkikingg.gitclient.GitRepoDetail.DI.Module.NetworkModule;
import com.ikkikingg.gitclient.GitRepoDetail.DI.Module.RepositoryModule;
import com.ikkikingg.gitclient.GitRepoDetail.GitRepoDetailViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {AppModule.class, NetworkModule.class, RepositoryModule.class, GitRepoDetailApiModule.class, DaoModule.class}
)

public interface GitRepoDetailComponent {
    public void inject(GitRepoDetailViewModel viewModelModule);
    public void inject(Context context);
}
