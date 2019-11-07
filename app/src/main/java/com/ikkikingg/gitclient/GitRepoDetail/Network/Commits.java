package com.ikkikingg.gitclient.GitRepoDetail.Network;

import androidx.room.Embedded;

import com.google.gson.annotations.SerializedName;

public class Commits {

    @SerializedName("sha")
    private String sha;
    @Embedded
    private Commit commit;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }
}
