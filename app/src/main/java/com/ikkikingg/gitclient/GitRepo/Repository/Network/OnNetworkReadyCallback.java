package com.ikkikingg.gitclient.GitRepo.Repository.Network;

import com.ikkikingg.gitclient.GitRepo.Model.GitRepo;

import java.util.List;

public interface OnNetworkReadyCallback {
    void onNetworkDataReady(List<GitRepo> gitRepos);
}
