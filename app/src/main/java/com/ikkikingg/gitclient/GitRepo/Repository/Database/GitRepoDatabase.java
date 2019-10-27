package com.ikkikingg.gitclient.GitRepo.Repository.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ikkikingg.gitclient.GitRepo.Model.GitRepo;

@Database(entities = {GitRepo.class}, version = 1, exportSchema = false)
public abstract class GitRepoDatabase extends RoomDatabase {

    private static GitRepoDatabase instance;

    public abstract GitRepoDao gitRepoDao();

    public static synchronized GitRepoDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    GitRepoDatabase.class, "git_repo_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
