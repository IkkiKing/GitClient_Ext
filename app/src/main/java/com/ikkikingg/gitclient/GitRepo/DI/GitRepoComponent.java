package com.ikkikingg.gitclient.GitRepo.DI;


import android.content.Context;

import com.ikkikingg.gitclient.GitRepo.ViewModel.GitRepoViewModel;
import com.ikkikingg.gitclient.GitRepo.DI.Module.AppModule;
import com.ikkikingg.gitclient.GitRepo.DI.Module.DaoModule;
import com.ikkikingg.gitclient.GitRepo.DI.Module.GitHubRepoApiModule;
import com.ikkikingg.gitclient.GitRepo.DI.Module.NetworkModule;
import com.ikkikingg.gitclient.GitRepo.DI.Module.RepositoryModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {AppModule.class, NetworkModule.class, RepositoryModule.class, GitHubRepoApiModule.class, DaoModule.class}
)

public interface GitRepoComponent {
    public void inject(GitRepoViewModel viewModelModule);
    public void inject(Context context);
}
