package com.ikkikingg.gitclient.GitRepo.Repository.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ikkikingg.gitclient.GitRepo.Model.GitRepo;

import java.util.List;

@Dao
public interface GitRepoDao {

    @Insert
    void insert(GitRepo gitRepo);

    @Update
    void update(GitRepo gitRepo);

    @Delete
    void delete(GitRepo gitRepo);

    @Query("DELETE FROM repo_table")
    void deleteAllRepos();

    @Query("SELECT * FROM repo_table")
    LiveData<List<GitRepo>> getAllRepos();

    @Query("SELECT * FROM repo_table WHERE  id = :id LIMIT 1")
    GitRepo getGitRepoById(int id);
}
