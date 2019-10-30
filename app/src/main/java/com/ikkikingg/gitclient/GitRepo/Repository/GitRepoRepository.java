package com.ikkikingg.gitclient.GitRepo.Repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import com.ikkikingg.gitclient.GitRepo.Database.GitRepoDao;
import com.ikkikingg.gitclient.GitRepo.Model.GitRepo;
import com.ikkikingg.gitclient.GitRepo.Network.ApiResponse;
import com.ikkikingg.gitclient.GitRepo.Network.GitHubRepoApi;
import com.ikkikingg.gitclient.GitRepo.Network.Resource;
import com.ikkikingg.gitclient.NetworkBoundResource.NetworkBoundResource;
import java.util.List;

import javax.inject.Inject;


public class GitRepoRepository {
    final private GitHubRepoApi gitHubRepoApi;
    final private GitRepoDao gitRepoDao;
    final private Context context;

    @Inject
    public GitRepoRepository(GitHubRepoApi gitHubRepoApi,
                             GitRepoDao gitRepoDao,
                             Context context) {
        this.gitHubRepoApi = gitHubRepoApi;
        this.gitRepoDao = gitRepoDao;
        this.context = context;
    }


    public LiveData<Resource<List<GitRepo>>> getAllRepos(final boolean networkRequest) {

        LiveData<Resource<List<GitRepo>>> liveData = new NetworkBoundResource<List<GitRepo>, List<GitRepo>>() {
            @Override
            protected void saveCallResult(@NonNull List<GitRepo> items) {
                GitRepo[] arr = new GitRepo[items.size()];
                items.toArray(arr);
                gitRepoDao.insertAll(arr);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<GitRepo> data) {
                return (data == null || data.isEmpty() || networkRequest);//let's always refresh to be up to date. data == null || data.isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<List<GitRepo>> loadFromDb() {
                return gitRepoDao.getAllRepos();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<GitRepo>>> createCall() {
                LiveData<ApiResponse<List<GitRepo>>> response = gitHubRepoApi.getAllReposLiveData();
                return response;
            }
        }.getAsLiveData();

        return liveData;
    }

    /*public LiveData<Resource<GitHubResponse>> getAllRepos(final boolean networkRequest) {
        //В отдельном потоке делаем запрос на обновление данных
        executor.execute(new Runnable() {
            @Override
            public void run() {
                refresh(networkRequest);
            }
        });

        final LiveData<List<GitRepo>> source = gitRepoDao.getAllRepos();

        final MediatorLiveData mediator = new MediatorLiveData();
        mediator.addSource(source, new Observer<List<GitRepo>>() {
            @Override
            public void onChanged(@Nullable List<GitRepo> gitRepoList) {
                Log.d("DATA", "Observed");
                GitHubResponse response = new GitHubResponse(gitRepoList);
                Resource<GitHubResponse> success = Resource.success(response);
                mediator.setValue(success);
            }
        });
        return mediator;
    }

    @WorkerThread
    private void refresh(final boolean networkRequest) {
        try {

            if (!networkRequest) {
                //Смотрим а есть ли у нас в БД данные, если есть подгружаем их и выходим
                List<GitRepo> listGitRepo = gitRepoDao.hasRepos();
                if (listGitRepo != null && !listGitRepo.isEmpty()) {
                    Log.d("DATA", "Room has a data");
                    return;
                } else {
                    Log.d("DATA", "Room is empty, Fetching data from server");
                }
            }

            //Если нет делаем запрос на сервер

            //Создаём запрос, получаем ответ, сохраняем в базу
            Response<List<GitRepo>> response = gitHubRepoApi.getAllRepos().execute();

            if (!response.isSuccessful()) {

                List<GitRepo> repoList = response.body();

                if (repoList != null) {
                    GitRepo[] repoArray = new GitRepo[repoList.size()];
                    repoList.toArray(repoArray);

                    long[] ids = gitRepoDao.insertAll(repoArray);

                    if (ids == null || ids.length != repoArray.length) {
                        Log.e("API", "Unable to insert");
                    } else {
                        Log.d("DATA", "Data inserted");
                    }
                } else {
                    Log.d("API", "Getting null response from a server");
                }
            }
        } catch (IOException e) {
            Log.e("API", "" + e.getMessage());
        }
    }*/




}
