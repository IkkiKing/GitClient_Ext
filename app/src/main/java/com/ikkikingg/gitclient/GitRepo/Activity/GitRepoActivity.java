package com.ikkikingg.gitclient.GitRepo.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.ikkikingg.gitclient.BrowseConnection;
import com.ikkikingg.gitclient.GitRepo.DI.DaggerGitAppComponent;
import com.ikkikingg.gitclient.GitRepo.Network.GitHubResponse;
import com.ikkikingg.gitclient.GitRepo.Network.Resource;
import com.ikkikingg.gitclient.GitRepo.ViewModel.GitRepoViewModel;
import com.ikkikingg.gitclient.GitRepo.Model.GitRepo;
import com.ikkikingg.gitclient.GitRepo.Adapter.GitRepoAdapter;

import com.ikkikingg.gitclient.GitRepo.DI.GitAppComponent;
import com.ikkikingg.gitclient.GitRepo.DI.Module.AppModule;
import com.ikkikingg.gitclient.GitRepo.DI.Module.DaoModule;
import com.ikkikingg.gitclient.GitRepo.DI.Module.GitHubRepoApiModule;
import com.ikkikingg.gitclient.GitRepo.DI.Module.NetworkModule;
import com.ikkikingg.gitclient.GitRepo.DI.Module.RepositoryModule;
import com.ikkikingg.gitclient.R;

public class GitRepoActivity extends AppCompatActivity {

    private GitRepoViewModel gitRepoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git_repo);

        //Прописываем зависимости
        GitAppComponent appComponent = DaggerGitAppComponent.builder()
                .appModule(new AppModule(getApplication()))
                .gitHubRepoApiModule(new GitHubRepoApiModule())
                .networkModule(new NetworkModule("https://api.github.com"))
                .daoModule(new DaoModule())
                .repositoryModule(new RepositoryModule(this))
                .build();


        //Создаём VM и инджектим
        gitRepoViewModel = GitRepoViewModel.create(this);
        appComponent.inject(gitRepoViewModel);


        final RecyclerView recyclerView = findViewById(R.id.recyclerViewRepos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final GitRepoAdapter adapter = new GitRepoAdapter(this);
        recyclerView.setAdapter(adapter);

        final SwipeRefreshLayout swipeRefresher = findViewById(R.id.swipeRefreshId);
        swipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gitRepoViewModel.load(true);
            }
        });

        gitRepoViewModel = ViewModelProviders.of(this).get(GitRepoViewModel.class);
        gitRepoViewModel.getAllRepos().observe(this, new Observer<Resource<GitHubResponse>>() {
            @Override
            public void onChanged(@Nullable Resource<GitHubResponse> gitHubResponseResource) {
                swipeRefresher.setRefreshing(false);
                switch (gitHubResponseResource.getStatus()) {
                    case LOADING: //loading
                        swipeRefresher.setRefreshing(true);
                        break;
                    case ERROR:
                        swipeRefresher.setRefreshing(false);

                        if (new BrowseConnection().isConnected()) {
                            final Snackbar snackbar = Snackbar.make(recyclerView, "Error loading data", Snackbar.LENGTH_LONG);
                            snackbar.setAction("Try again", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    gitRepoViewModel.load(true);
                                    snackbar.dismiss();
                                }
                            });
                        } else {
                            final Snackbar snackbar = Snackbar.make(recyclerView, "Internet connection is lost", Snackbar.LENGTH_LONG);
                            snackbar.setAction("Try again", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    gitRepoViewModel.load(true);
                                    snackbar.dismiss();
                                }
                            });
                        }
                        Log.e("Error", gitHubResponseResource.getException().getMessage(), gitHubResponseResource.getException());
                        Toast.makeText(GitRepoActivity.this, "Error loading data", Toast.LENGTH_LONG).show();

                        break;
                    case SUCCESS:
                        swipeRefresher.setRefreshing(false);
                        GitHubResponse data = gitHubResponseResource.getData();
                        adapter.submitList(data.getGitRepoList());
                        break;
                }
            }
        });
        gitRepoViewModel.load(false);


        adapter.setOnItemClickListener(new GitRepoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GitRepo gitRepo) {

            }
        });


    }


}
