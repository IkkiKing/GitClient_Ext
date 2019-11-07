package com.ikkikingg.gitclient.GitRepoDetail.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.ikkikingg.gitclient.GitRepoDetail.Model.GitRepoDetail;

@Database(entities = {GitRepoDetail.class}, version = 1, exportSchema = false)
public abstract class GitRepoDetailDatabase extends RoomDatabase {
    public abstract GitRepoDetailDao gitRepoDetailDao();
}
