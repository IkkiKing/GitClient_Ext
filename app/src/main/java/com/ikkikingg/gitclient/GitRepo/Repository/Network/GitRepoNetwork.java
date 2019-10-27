package com.ikkikingg.gitclient.GitRepo.Repository.Network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.ikkikingg.gitclient.GitRepo.Model.GitRepo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GitRepoNetwork {

    private static GitRepoNetwork instance;
    private Context context;
    private List<GitRepo> gitRepos = new ArrayList<>();
    private MutableLiveData<List<GitRepo>> LiveDataGitRepos = new MutableLiveData<>();
    private final Set<Target> protectedFromGarbageCollectorTargets = new HashSet<>();

    public GitRepoNetwork(Context context) {
        this.context = context;
    }

    public static GitRepoNetwork getInstance(Context context) {
        if (instance == null) {
            instance = new GitRepoNetwork(context);
        }
        return instance;
    }


    public MutableLiveData<List<GitRepo>> getAllRepos(final OnNetworkReadyCallback onNetworkReadyCallback) {

        GitHubRepoApi gitHubRepoApi = RetrofitInstance.getApiService();

        Call<List<Repo>> call = gitHubRepoApi.getAllRepos();

        call.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {

                if (response.isSuccessful()) {

                    List<Repo> repos = response.body();

                    for (Repo repo : repos) {

                        int id      = repo.getId();
                        String name = repo.getName();

                        String licence = "";
                        String login   = "";
                        String url     = "";

                        if (repo.getLicense() != null) {
                            licence = repo.getLicense().getName();
                        }
                        if (repo.getOwner() != null) {
                            login = repo.getOwner().getLogin();
                            url   = repo.getOwner().getAvatarUrl();
                        }

                        gitRepos.add(new GitRepo(id, name, licence, login, url));

                    }
                    imageDownload();
                    onNetworkReadyCallback.onNetworkDataReady(gitRepos);
                    LiveDataGitRepos.setValue(gitRepos);
                }
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {

            }
        });
        return LiveDataGitRepos;
    }

    public void imageDownload() {

        for (GitRepo gitRepo : gitRepos) {

            BitmapTarget mTarget = new BitmapTarget(gitRepo.getLogin());
            protectedFromGarbageCollectorTargets.add(mTarget);

            Picasso.get()
                    .load(gitRepo.getImage())
                    .into(mTarget);
        }
    }

    private class BitmapTarget implements Target {
        private final String login;

        public BitmapTarget(String login) {
            this.login = login;
        }

        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(context.getFilesDir().getPath() + "/" + login);
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            ostream.flush();
                            ostream.close();
                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                    protectedFromGarbageCollectorTargets.remove(this);
                }


            }).start();
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            protectedFromGarbageCollectorTargets.remove(this);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }
}
