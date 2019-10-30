package com.ikkikingg.gitclient.GitRepo.Network;

import com.ikkikingg.gitclient.GitRepo.Model.GitRepo;

import java.util.List;

public class GitHubResponse {

    final private List<GitRepo> gitRepoList;
    private boolean networkRequest;

    public GitHubResponse(List<GitRepo> gitRepoList, boolean networkRequest) {
        this.gitRepoList = gitRepoList;
        this.networkRequest = networkRequest;
    }

    public List<GitRepo> getGitRepoList() {
        return gitRepoList;
    }

    public void setNetworkRequest(boolean networkRequest) {
        this.networkRequest = networkRequest;
    }

    public boolean isNetworkRequest() {
        return networkRequest;
    }
}
