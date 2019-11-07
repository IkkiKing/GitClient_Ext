package com.ikkikingg.gitclient.GitRepoDetail.Network;

import com.ikkikingg.gitclient.GitRepoDetail.Model.GitRepoDetail;


public class GitRepoDetailResponse {

    final private GitRepoDetail gitRepoDetail;
    private boolean networkRequest;
    private String owner;
    private String repoName;

    public GitRepoDetailResponse(GitRepoDetail gitRepoDetail, boolean networkRequest, String owner, String repoName) {
        this.gitRepoDetail = gitRepoDetail;
        this.networkRequest = networkRequest;
        this.owner = owner;
        this.repoName = repoName;
    }

    public GitRepoDetail getGitRepoDetail() {
        return gitRepoDetail;
    }

    public void setNetworkRequest(boolean networkRequest) {
        this.networkRequest = networkRequest;
    }

    public boolean isNetworkRequest() {
        return networkRequest;
    }

    public String getOwner() {
        return owner;
    }

    public String getRepoName() {
        return repoName;
    }
}
