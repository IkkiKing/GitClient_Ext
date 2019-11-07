package com.ikkikingg.gitclient.GitRepoDetail.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.ikkikingg.gitclient.GitRepo.Model.GitRepo;
import com.ikkikingg.gitclient.GitRepoDetail.Model.GitRepoDetail;

import java.util.List;

@Dao
public interface GitRepoDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GitRepoDetail gitRepoDetail);

   /* @Query("UPDATE GitRepoDetail " +
            "  SET sha     = :p_sha, " +
            "      message = :p_message," +
            "      author  = :p_author," +
            "      data    = :p_data " +
            "WHERE id = :p_id")
    void updateCommitInfo(int p_id, String p_sha, String p_message, String p_author, String p_data);*/

    @Query("SELECT * FROM GitRepoDetail WHERE login = :login AND name = :name ")
    LiveData<GitRepoDetail> getGitRepoDetail(String login, String name);
}
