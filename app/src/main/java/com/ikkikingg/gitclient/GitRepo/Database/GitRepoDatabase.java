package com.ikkikingg.gitclient.GitRepo.Database;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.ikkikingg.gitclient.GitRepo.Model.GitRepo;

@Database(entities = {GitRepo.class}, version = 1, exportSchema = false)
public abstract class GitRepoDatabase extends RoomDatabase {
    public abstract GitRepoDao gitRepoDao();
}
