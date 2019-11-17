package com.ikkikingg.gitclient.GitRepoDetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.ikkikingg.gitclient.GitRepoDetail.Resource;
import com.ikkikingg.gitclient.GitRepoDetail.DI.DaggerGitRepoDetailComponent;
import com.ikkikingg.gitclient.GitRepoDetail.DI.GitRepoDetailComponent;
import com.ikkikingg.gitclient.GitRepoDetail.DI.Module.AppModule;
import com.ikkikingg.gitclient.GitRepoDetail.DI.Module.DaoModule;
import com.ikkikingg.gitclient.GitRepoDetail.DI.Module.GitRepoDetailApiModule;
import com.ikkikingg.gitclient.GitRepoDetail.DI.Module.NetworkModule;
import com.ikkikingg.gitclient.GitRepoDetail.DI.Module.RepositoryModule;
import com.ikkikingg.gitclient.GitRepoDetail.Model.GitRepoDetail;
import com.ikkikingg.gitclient.R;


public class GitRepoDetailActivity extends AppCompatActivity {

    private GitRepoDetailViewModel gitRepoDetailViewModel;
    private TextView textViewFullname;
    private TextView textViewDescription;
    private TextView textViewHash;
    private TextView textViewMessage;
    private TextView textViewAuthor;
    private TextView textViewData;
    private TextView textViewForks;
    private TextView textViewStars;
    private TextView textViewWatches;
    private SwipeRefreshLayout swipeRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git_repo_detail);

        //Прописываем зависимости
        GitRepoDetailComponent appComponent = DaggerGitRepoDetailComponent.builder()
                .appModule(new AppModule(getApplication()))
                .gitRepoDetailApiModule(new GitRepoDetailApiModule())
                .networkModule(new NetworkModule("https://api.github.com"))
                .daoModule(new DaoModule())
                .repositoryModule(new RepositoryModule(this))
                .build();

        textViewFullname = findViewById(R.id.textViewFullName);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewHash = findViewById(R.id.textViewHash);
        textViewMessage = findViewById(R.id.textViewMessage);
        textViewAuthor = findViewById(R.id.textViewAuthor);
        textViewData = findViewById(R.id.textViewData);
        textViewForks = findViewById(R.id.textViewForks);
        textViewStars = findViewById(R.id.textViewStars);
        textViewWatches = findViewById(R.id.textViewWatches);
        swipeRefresh = findViewById(R.id.swipeRefreshId);


        gitRepoDetailViewModel = GitRepoDetailViewModel.create(this);
        appComponent.inject(gitRepoDetailViewModel);

        Intent intent = getIntent();

        final String repoName = intent.getStringExtra("repoName");
        final String repoLogin = intent.getStringExtra("repoLogin");


        gitRepoDetailViewModel = ViewModelProviders.of(this).get(GitRepoDetailViewModel.class);

        getRepoDetails();

        getDetails(repoLogin, repoName);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDetails(repoLogin, repoName);
            }
        });
    }


    private void getRepoDetails() {
        gitRepoDetailViewModel.getRepoDetails().observe(this, new Observer<Resource<GitRepoDetail>>() {
            @Override
            public void onChanged(Resource<GitRepoDetail> gitRepoDetails) {
                switch (gitRepoDetails.getStatus()) {
                    case LOADING: //loading
                        swipeRefresh.setRefreshing(true);
                        break;
                    case ERROR:
                        swipeRefresh.setRefreshing(false);
                        Toast.makeText(GitRepoDetailActivity.this, "Error?", Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        swipeRefresh.setRefreshing(false);
                        GitRepoDetail gitRepoDetail = gitRepoDetails.getData();
                        if (gitRepoDetail != null) {
                            textViewFullname.setText(gitRepoDetail.getFullName());
                            textViewDescription.setText(gitRepoDetail.getDescription());
                            textViewForks.setText(String.valueOf(gitRepoDetail.getForksCount()));
                            textViewStars.setText(String.valueOf(gitRepoDetail.getStargazersCount()));
                            textViewWatches.setText(String.valueOf(gitRepoDetail.getWatchersCount()));

                            textViewHash.setText(gitRepoDetail.getSha());
                            textViewMessage.setText(gitRepoDetail.getMessage());
                            textViewAuthor.setText(gitRepoDetail.getAuthor());
                            textViewData.setText(gitRepoDetail.getData());

                        }
                        break;

                }
            }
        });
    }


    private void getDetails(String repoLogin, String repoName) {
        gitRepoDetailViewModel.getDetails(true, repoLogin, repoName);
    }
}
