package com.ikkikingg.gitclient.GitRepo.DI.Module;

import android.app.Application;

import androidx.room.Room;

import com.ikkikingg.gitclient.GitRepo.Database.GitRepoDao;
import com.ikkikingg.gitclient.GitRepo.Database.GitRepoDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DaoModule {

    @Provides
    @Singleton
    public GitRepoDao provideGitRepoDao(Application app){
        GitRepoDatabase db = Room.databaseBuilder(app, GitRepoDatabase.class, "github-db").build();
        return db.gitRepoDao();
    }


}
