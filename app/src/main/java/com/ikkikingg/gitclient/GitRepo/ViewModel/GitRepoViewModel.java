package com.ikkikingg.gitclient.GitRepo.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ikkikingg.gitclient.GitRepo.Model.GitRepo;
import com.ikkikingg.gitclient.GitRepo.Repository.GitRepoRepository;

import java.util.List;

public class GitRepoViewModel extends AndroidViewModel {

    private GitRepoRepository repository;
    private LiveData<List<GitRepo>> allRepos;

    public GitRepoViewModel(@NonNull Application application) {
        super(application);
        repository = new GitRepoRepository(application);
        allRepos   = repository.getAllRepos();
    }

    public void insert(GitRepo gitRepo){
        repository.insert(gitRepo);
    }

    public void update(GitRepo gitRepo){
        repository.update(gitRepo);
    }

    public void delete(GitRepo gitRepo){
        repository.delete(gitRepo);
    }

    public void deleteAllGitRepo(){
        repository.deleteAll();
    }

    public LiveData<List<GitRepo>> getAllRepos(){
        return allRepos;
    }


}
