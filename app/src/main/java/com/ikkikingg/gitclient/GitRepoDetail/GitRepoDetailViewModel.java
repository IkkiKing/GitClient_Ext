package com.ikkikingg.gitclient.GitRepoDetail;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import com.ikkikingg.gitclient.GitRepoDetail.Model.GitRepoDetail;
import com.ikkikingg.gitclient.GitRepoDetail.Repository.GitRepoDetailRepository;
import javax.inject.Inject;

public class GitRepoDetailViewModel extends ViewModel {

    private GitRepoDetailRepository repository;


    public static GitRepoDetailViewModel create(FragmentActivity activity) {
        GitRepoDetailViewModel viewModel = ViewModelProviders.of(activity).get(GitRepoDetailViewModel.class);
        return viewModel;
    }

    @Inject
    public void setRepository(GitRepoDetailRepository repository) {
        this.repository = repository;
    }

    public LiveData<Resource<GitRepoDetail>> getRepoDetails() {
        return repository.getRepoDetails();
    }

    public void getDetails(final boolean networkRequest,
                           final String owner,
                           final String repoName){

        repository.getDetails(networkRequest, owner, repoName);

    }

}
