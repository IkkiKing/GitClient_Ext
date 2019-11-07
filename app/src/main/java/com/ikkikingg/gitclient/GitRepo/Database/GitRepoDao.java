package com.ikkikingg.gitclient.GitRepo.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ikkikingg.gitclient.GitRepo.Model.GitRepo;

import java.util.List;

@Dao
public interface GitRepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(GitRepo... gitRepos);

    @Query("SELECT * FROM GitRepo")
    LiveData<List<GitRepo>> getAllRepos();

    @Insert
    void insert(GitRepo gitRepo);

    @Update
    void update(GitRepo gitRepo);

    @Delete
    void delete(GitRepo gitRepo);

    @Query("DELETE FROM GitRepo")
    void deleteAllRepos();


    @Query("SELECT * FROM GitRepo WHERE  id = :id LIMIT 1")
    GitRepo getGitRepoById(int id);




}
