package com.ikkikingg.gitclient.GitRepo.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.android.material.snackbar.Snackbar;
import com.ikkikingg.gitclient.GitRepo.Adapter.CustomRecyclerView;


import com.ikkikingg.gitclient.GitRepo.DI.DaggerGitRepoComponent;
import com.ikkikingg.gitclient.GitRepo.DI.GitRepoComponent;
import com.ikkikingg.gitclient.GitRepo.Network.GitRepoResponse;
import com.ikkikingg.gitclient.GitRepo.Network.Resource;
import com.ikkikingg.gitclient.GitRepo.ViewModel.GitRepoViewModel;
import com.ikkikingg.gitclient.GitRepo.Model.GitRepo;
import com.ikkikingg.gitclient.GitRepo.Adapter.GitRepoAdapter;



import com.ikkikingg.gitclient.GitRepo.DI.Module.AppModule;
import com.ikkikingg.gitclient.GitRepo.DI.Module.DaoModule;
import com.ikkikingg.gitclient.GitRepo.DI.Module.GitHubRepoApiModule;
import com.ikkikingg.gitclient.GitRepo.DI.Module.NetworkModule;
import com.ikkikingg.gitclient.GitRepo.DI.Module.RepositoryModule;

import com.ikkikingg.gitclient.GitRepoDetail.GitRepoDetailActivity;
import com.ikkikingg.gitclient.R;

public class GitRepoActivity extends AppCompatActivity {

    private GitRepoViewModel gitRepoViewModel;
    private SwipeRefreshLayout swipeRefresher;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git_repo);

        //Прописываем зависимости
        GitRepoComponent appComponent = DaggerGitRepoComponent.builder()
                .appModule(new AppModule(getApplication()))
                .gitHubRepoApiModule(new GitHubRepoApiModule())
                .networkModule(new NetworkModule("https://api.github.com"))
                .daoModule(new DaoModule())
                .repositoryModule(new RepositoryModule(this))
                .build();


        //Создаём VM и инджектим
        gitRepoViewModel = GitRepoViewModel.create(this);
        appComponent.inject(gitRepoViewModel);

        final CustomRecyclerView recyclerView = findViewById(R.id.recyclerViewRepos);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setEmptyView(findViewById(R.id.emptyImageView));

        final GitRepoAdapter adapter = new GitRepoAdapter(this);
        recyclerView.setAdapter(adapter);

        swipeRefresher = findViewById(R.id.swipeRefreshId);
        swipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gitRepoViewModel.load(true);
            }
        });

        gitRepoViewModel = ViewModelProviders.of(this).get(GitRepoViewModel.class);
        gitRepoViewModel.getAllRepos().observe(this, new Observer<Resource<GitRepoResponse>>() {
            @Override
            public void onChanged(@Nullable Resource<GitRepoResponse> gitHubResponseResource) {
                swipeRefresher.setRefreshing(false);

                switch (gitHubResponseResource.getStatus()) {
                    case LOADING: //loading
                        swipeRefresher.setRefreshing(true);
                        break;
                    case ERROR:
                        swipeRefresher.setRefreshing(false);
                        //Если получили ошибку но соединение есть показываем снек бар
                        if (isOnline(getApplicationContext())) {
                            getSnackbar(swipeRefresher, "Error loading data:" , "Try again", Snackbar.LENGTH_INDEFINITE);
                        }

                        Log.e("Error", gitHubResponseResource.getException().getMessage(), gitHubResponseResource.getException());
                        break;
                    case SUCCESS:
                        swipeRefresher.setRefreshing(false);
                        GitRepoResponse data = gitHubResponseResource.getData();
                        adapter.submitList(data.getGitRepoList());

                        break;
                }
            }
        });
        gitRepoViewModel.load(false);


        adapter.setOnItemClickListener(new GitRepoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GitRepo gitRepo) {
                Intent intent = new Intent(getApplicationContext(), GitRepoDetailActivity.class);
                intent.putExtra("repoName", gitRepo.getName());
                intent.putExtra("repoLogin", gitRepo.getOwner().getLogin());
                startActivity(intent);
            }
        });
    }

    private BroadcastReceiver internetConnectionReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isOnline(context) && swipeRefresher != null) {
                getSnackbar(swipeRefresher, "Internet connection is lost", "Try again", Snackbar.LENGTH_INDEFINITE);
            }
        }
    };

    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void getSnackbar(View view, String text, String textAction, int length) {
        snackbar = Snackbar.make(view, text, length);
        if (textAction != null && !textAction.isEmpty()) {
            snackbar.setAction(textAction, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gitRepoViewModel.load(true);
                    snackbar.dismiss();
                }
            });
        }
        snackbar.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(internetConnectionReciever, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(internetConnectionReciever);
    }
}
