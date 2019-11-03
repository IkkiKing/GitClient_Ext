package com.ikkikingg.gitclient.GitRepo.ViewModel;

import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import com.ikkikingg.gitclient.GitRepo.Model.GitRepo;
import com.ikkikingg.gitclient.GitRepo.Repository.GitRepoRepository;
import com.ikkikingg.gitclient.GitRepo.Network.GitHubResponse;
import com.ikkikingg.gitclient.GitRepo.Network.Resource;
import java.util.List;
import javax.inject.Inject;

public class GitRepoViewModel extends ViewModel {

    private GitRepoRepository repository;

    final private MutableLiveData<Request> request = new MutableLiveData();

    final private LiveData<Resource<GitHubResponse>> allRepos = Transformations.switchMap(request, new Function<Request, LiveData<Resource<GitHubResponse>>>() {
        @Override
        public LiveData<Resource<GitHubResponse>> apply(final Request input) {

            LiveData<Resource<List<GitRepo>>> resourceLiveData = repository.getAllRepos(input.networkRequest);

            final MediatorLiveData<Resource<GitHubResponse>> mediator = new MediatorLiveData<>();

            mediator.addSource(resourceLiveData, new Observer<Resource<List<GitRepo>>>() {
                @Override
                public void onChanged(@Nullable Resource<List<GitRepo>> gitRepos) {
                    GitHubResponse resp = new GitHubResponse(gitRepos.getData(), input.networkRequest);
                    Resource<GitHubResponse> response = null;
                    switch (gitRepos.getStatus()){
                        case LOADING:
                            response =  Resource.loading(resp);
                            break;
                        case SUCCESS:
                            response =  Resource.success(resp);
                            break;
                        case ERROR:
                            response =  Resource.error(gitRepos.getException(), null);
                            break;

                    }
                    mediator.setValue(response);
                }
            });
            return mediator;
        }
    });


    public static GitRepoViewModel create(FragmentActivity activity) {
        GitRepoViewModel viewModel = ViewModelProviders.of(activity).get(GitRepoViewModel.class);
        return viewModel;
    }

    @Inject
    public void setRepository(GitRepoRepository repository) {
        this.repository = repository;
    }

    public LiveData<Resource<GitHubResponse>> getAllRepos(){
        return allRepos;
    }

    public void load(boolean networkRequest) {
        request.setValue(new Request(networkRequest));
    }

    public static class Request {

        final private boolean networkRequest;

        public Request(boolean networkRequest) {
            this.networkRequest = networkRequest;
        }
        public boolean getRequest() {
            return networkRequest;
        }
    }

}
