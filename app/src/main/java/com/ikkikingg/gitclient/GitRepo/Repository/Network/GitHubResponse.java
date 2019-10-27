package com.ikkikingg.gitclient.GitRepo.Repository.Network;

import com.ikkikingg.gitclient.GitRepo.Model.GitRepo;

import java.util.List;

public class GitHubResponse {
    final private List<GitRepo> gitRepoList;

    public GitHubResponse(List<GitRepo> gitRepoList) {
        this.gitRepoList = gitRepoList;
    }

    public List<GitRepo> getGitRepoList() {
        return gitRepoList;
    }
}
