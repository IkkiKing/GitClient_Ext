package com.ikkikingg.gitclient.GitRepoDetail;

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
import com.ikkikingg.gitclient.GitRepo.Network.Resource;
import com.ikkikingg.gitclient.GitRepoDetail.Model.GitRepoDetail;
import com.ikkikingg.gitclient.GitRepoDetail.Network.GitRepoDetailResponse;
import com.ikkikingg.gitclient.GitRepoDetail.Repository.GitRepoDetailRepository;
import javax.inject.Inject;

public class GitRepoDetailViewModel extends ViewModel {

    private GitRepoDetailRepository repository;

    final private MutableLiveData<Request> request = new MutableLiveData();

    final private LiveData<Resource<GitRepoDetailResponse>> repoDetails = Transformations.switchMap(request, new Function<Request, LiveData<Resource<GitRepoDetailResponse>>>() {
        @Override
        public LiveData<Resource<GitRepoDetailResponse>> apply(final Request input) {

            LiveData<Resource<GitRepoDetail>> resourceLiveData = repository.getRepoDetails(input.networkRequest, input.owner, input.repoName);

            final MediatorLiveData<Resource<GitRepoDetailResponse>> mediator = new MediatorLiveData<>();

            mediator.addSource(resourceLiveData, new Observer<Resource<GitRepoDetail>>() {
                @Override
                public void onChanged(@Nullable Resource<GitRepoDetail> gitRepoDetail) {
                    GitRepoDetailResponse resp = new GitRepoDetailResponse(gitRepoDetail.getData(), input.networkRequest, input.owner, input.repoName);
                    Resource<GitRepoDetailResponse> response = null;
                    switch (gitRepoDetail.getStatus()) {
                        case LOADING:
                            response = Resource.loading(resp);
                            break;
                        case SUCCESS:
                            response = Resource.success(resp);
                            break;
                        case ERROR:
                            response = Resource.error(gitRepoDetail.getException(), null);
                            break;
                    }
                    mediator.setValue(response);
                }
            });
            return mediator;
        }
    });


    public static GitRepoDetailViewModel create(FragmentActivity activity) {
        GitRepoDetailViewModel viewModel = ViewModelProviders.of(activity).get(GitRepoDetailViewModel.class);
        return viewModel;
    }

    @Inject
    public void setRepository(GitRepoDetailRepository repository) {
        this.repository = repository;
    }

    public LiveData<Resource<GitRepoDetailResponse>> getRepoDetails() {
        return repoDetails;
    }

    public void load(boolean networkRequest, String owner, String repoName) {
        request.setValue(new Request(networkRequest, owner, repoName));
    }


    public static class Request {
        final private boolean networkRequest;
        final private String owner;
        final private String repoName;

        public Request(boolean networkRequest, String owner, String repoName) {
            this.networkRequest = networkRequest;
            this.owner = owner;
            this.repoName = repoName;
        }

        public boolean getRequest() {
            return networkRequest;
        }
        public String getOwner() {
            return owner;
        }
        public String getRepoName() {
            return repoName;
        }
    }
}
