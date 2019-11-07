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
import com.ikkikingg.gitclient.GitRepo.Network.GitRepoResponse;
import com.ikkikingg.gitclient.GitRepo.Network.Resource;
import java.util.List;
import javax.inject.Inject;

public class GitRepoViewModel extends ViewModel {

    private GitRepoRepository repository;

    final private MutableLiveData<Request> request = new MutableLiveData();

    final private LiveData<Resource<GitRepoResponse>> allRepos = Transformations.switchMap(request, new Function<Request, LiveData<Resource<GitRepoResponse>>>() {
        @Override
        public LiveData<Resource<GitRepoResponse>> apply(final Request input) {

            LiveData<Resource<List<GitRepo>>> resourceLiveData = repository.getAllRepos(input.networkRequest);

            final MediatorLiveData<Resource<GitRepoResponse>> mediator = new MediatorLiveData<>();

            mediator.addSource(resourceLiveData, new Observer<Resource<List<GitRepo>>>() {
                @Override
                public void onChanged(@Nullable Resource<List<GitRepo>> gitRepos) {
                    GitRepoResponse resp = new GitRepoResponse(gitRepos.getData(), input.networkRequest);
                    Resource<GitRepoResponse> response = null;
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

    public LiveData<Resource<GitRepoResponse>> getAllRepos(){
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
