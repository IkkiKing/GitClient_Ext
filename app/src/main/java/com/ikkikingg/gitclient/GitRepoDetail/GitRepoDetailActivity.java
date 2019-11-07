package com.ikkikingg.gitclient.GitRepoDetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ikkikingg.gitclient.GitRepo.Network.Resource;
import com.ikkikingg.gitclient.GitRepoDetail.DI.DaggerGitRepoDetailComponent;
import com.ikkikingg.gitclient.GitRepoDetail.DI.GitRepoDetailComponent;
import com.ikkikingg.gitclient.GitRepoDetail.DI.Module.AppModule;
import com.ikkikingg.gitclient.GitRepoDetail.DI.Module.DaoModule;
import com.ikkikingg.gitclient.GitRepoDetail.DI.Module.GitRepoDetailApiModule;
import com.ikkikingg.gitclient.GitRepoDetail.DI.Module.NetworkModule;
import com.ikkikingg.gitclient.GitRepoDetail.DI.Module.RepositoryModule;
import com.ikkikingg.gitclient.GitRepoDetail.Model.GitRepoDetail;
import com.ikkikingg.gitclient.GitRepoDetail.Network.GitRepoDetailResponse;
import com.ikkikingg.gitclient.R;


public class GitRepoDetailActivity extends AppCompatActivity {

    private GitRepoDetailViewModel gitRepoDetailViewModel;

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

        gitRepoDetailViewModel = GitRepoDetailViewModel.create(this);
        appComponent.inject(gitRepoDetailViewModel);

        Intent intent = getIntent();

        String repoName = intent.getStringExtra("repoName");
        String repoLogin = intent.getStringExtra("repoLogin");

        final TextView textViewFullname = findViewById(R.id.textViewFullName);
        final TextView textViewDescription = findViewById(R.id.textViewDescription);
        final TextView textViewHash = findViewById(R.id.textViewHash);
        final TextView textViewMessage = findViewById(R.id.textViewMessage);
        final TextView textViewAuthor = findViewById(R.id.textViewAuthor);
        final TextView textViewData = findViewById(R.id.textViewData);
        final TextView textViewForks = findViewById(R.id.textViewForks);
        final TextView textViewStars = findViewById(R.id.textViewStars);
        final TextView textViewWatches = findViewById(R.id.textViewWatches);

        gitRepoDetailViewModel = ViewModelProviders.of(this).get(GitRepoDetailViewModel.class);
        gitRepoDetailViewModel.getRepoDetails().observe(this, new Observer<Resource<GitRepoDetailResponse>>() {
            @Override
            public void onChanged(Resource<GitRepoDetailResponse> gitRepoDetailResponseResource) {
                switch (gitRepoDetailResponseResource.getStatus()) {
                    case LOADING: //loading
                        break;
                    case ERROR:
                        Log.e("Error", gitRepoDetailResponseResource.getException().getMessage(), gitRepoDetailResponseResource.getException());
                        break;
                    case SUCCESS:
                        GitRepoDetail gitRepoDetail = gitRepoDetailResponseResource.getData().getGitRepoDetail();

                        textViewFullname.setText(gitRepoDetail.getFullName());
                        textViewDescription.setText(gitRepoDetail.getDescription());
                        textViewForks.setText(String.valueOf(gitRepoDetail.getForksCount()));
                        textViewStars.setText(String.valueOf(gitRepoDetail.getStargazersCount()));
                        textViewWatches.setText(String.valueOf(gitRepoDetail.getWatchersCount()));

                        break;
                }
            }
        });

        gitRepoDetailViewModel.load(true, repoLogin, repoName);

    }
}
