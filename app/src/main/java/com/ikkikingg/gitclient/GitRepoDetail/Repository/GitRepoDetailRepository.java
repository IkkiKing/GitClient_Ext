package com.ikkikingg.gitclient.GitRepoDetail.Repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.ikkikingg.gitclient.GitRepoDetail.Model.GitRepo;
import com.ikkikingg.gitclient.GitRepoDetail.Network.Commit;
import com.ikkikingg.gitclient.GitRepoDetail.Network.Commits;
import com.ikkikingg.gitclient.GitRepoDetail.Resource;
import com.ikkikingg.gitclient.GitRepoDetail.Database.GitRepoDetailDao;
import com.ikkikingg.gitclient.GitRepoDetail.Model.GitRepoDetail;
import com.ikkikingg.gitclient.GitRepoDetail.Network.GitRepoDetailApi;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class GitRepoDetailRepository {
    final private GitRepoDetailApi gitRepoDetailApi;
    final private GitRepoDetailDao gitRepoDetailDao;
    final private Context context;

    private MediatorLiveData<Resource<GitRepoDetail>> gitRepoDetails = new MediatorLiveData<>();


    @Inject
    public GitRepoDetailRepository(GitRepoDetailApi gitRepoDetailApi,
                                   GitRepoDetailDao gitRepoDetailDao,
                                   Context context) {
        this.gitRepoDetailApi = gitRepoDetailApi;
        this.gitRepoDetailDao = gitRepoDetailDao;
        this.context = context;
    }


    public LiveData<Resource<GitRepoDetail>> getRepoDetails() {
        return gitRepoDetails;
    }




    public void getDetails(final boolean networkRequest,
                           final String owner,
                           final String repoName) {

        LiveData<Resource<GitRepoDetail>> dbSource = LiveDataReactiveStreams.fromPublisher(
                gitRepoDetailDao.getGitRepoDetailRx(owner, repoName)
                        .map(new Function<GitRepoDetail, Resource<GitRepoDetail>>() {
                            @Override
                            public Resource<GitRepoDetail> apply(GitRepoDetail gitRepoDetail) throws Exception {
                                Log.d("REPO", "FETCHED FROM DB");
                                return Resource.success(gitRepoDetail);
                            }
                        }).onErrorReturn(new Function<Throwable, Resource<GitRepoDetail>>() {
                    @Override
                    public Resource<GitRepoDetail> apply(Throwable throwable) throws Exception {
                        return Resource.error("Could not get data from network", null);
                    }
                }).subscribeOn(Schedulers.io())
        );

        LiveData<Resource<GitRepoDetail>> source = LiveDataReactiveStreams.fromPublisher(
                gitRepoDetailApi.getRepoDetail(owner, repoName)
                        .flatMap(new Function<GitRepoDetail, Publisher<? extends Resource<GitRepoDetail>>>() {
                            @Override
                            public Publisher<? extends Resource<GitRepoDetail>> apply(GitRepoDetail gitRepoDetail) throws Exception {
                                return gitRepoDetailApi.getCommits(owner, repoName)
                                        .map(new Function<List<Commits>, Resource<GitRepoDetail>>() {
                                            @Override
                                            public Resource<GitRepoDetail> apply(List<Commits> commits) throws Exception {
                                                Commits commit = commits.get(0);

                                                if (commit != null) {
                                                    gitRepoDetail.setSha(commit.getSha());
                                                    gitRepoDetail.setMessage(commit.getCommit().getMessage());
                                                    gitRepoDetail.setAuthor(commit.getCommit().getAuthor().getName());
                                                    gitRepoDetail.setData(commit.getCommit().getAuthor().getDate());

                                                }
                                                return Resource.loading(gitRepoDetail);
                                            }
                                        });
                            }
                        }).onErrorReturn(new Function<Throwable, Resource<GitRepoDetail>>() {
                    @Override
                    public Resource<GitRepoDetail> apply(Throwable throwable) throws Exception {
                        return Resource.error("Could not get data from network", null);
                    }
                }).map(new Function<Resource<GitRepoDetail>, Resource<GitRepoDetail>>() {
                    @Override
                    public Resource<GitRepoDetail> apply(Resource<GitRepoDetail> gitRepoDetailResource) throws Exception {
                        Log.d("REPO", "FETCHED FROM NETWORK");
                        gitRepoDetailDao.insert(gitRepoDetailResource.getData());
                        return Resource.success(gitRepoDetailResource.getData());
                    }
                }).subscribeOn(Schedulers.io())
        );




        gitRepoDetails.addSource(dbSource, new Observer<Resource<GitRepoDetail>>() {
            @Override
            public void onChanged(Resource<GitRepoDetail> gitRepoDetail) {

                gitRepoDetails.setValue(Resource.loading((GitRepoDetail) null));

                if (gitRepoDetail != null && !networkRequest){
                    gitRepoDetails.setValue(gitRepoDetail);

                }else{
                    gitRepoDetails.addSource(source, new Observer<Resource<GitRepoDetail>>() {
                        @Override
                        public void onChanged(Resource<GitRepoDetail> gitRepoDetailResource) {
                            gitRepoDetails.setValue(gitRepoDetailResource);
                            gitRepoDetails.removeSource(source);
                        }
                    });
                }
                gitRepoDetails.removeSource(dbSource);
            }
        });






    }
}
