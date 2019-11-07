package com.ikkikingg.gitclient.GitRepoDetail.DI.Module;

import android.app.Application;

import androidx.room.Room;

import com.ikkikingg.gitclient.GitRepoDetail.Database.GitRepoDetailDao;
import com.ikkikingg.gitclient.GitRepoDetail.Database.GitRepoDetailDatabase;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class DaoModule {

    @Provides
    @Singleton
    public GitRepoDetailDao provideGitRepoDetailDao(Application app){
        GitRepoDetailDatabase db = Room.databaseBuilder(app, GitRepoDetailDatabase.class, "github-detail-db").build();
        return db.gitRepoDetailDao();
    }


}
