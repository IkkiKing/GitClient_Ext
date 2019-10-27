package com.ikkikingg.gitclient.GitRepo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import com.ikkikingg.gitclient.GitRepo.Repository.Network.GitHubResponse;
import com.ikkikingg.gitclient.GitRepo.Repository.Network.Resource;
import com.ikkikingg.gitclient.GitRepo.ViewModel.GitRepoViewModel;
import com.ikkikingg.gitclient.GitRepo.Model.GitRepo;
import com.ikkikingg.gitclient.GitRepo.Adapter.GitRepoAdapter;
import com.ikkikingg.gitclient.R;
import java.util.List;

public class GitRepoActivity extends AppCompatActivity {

    private GitRepoViewModel gitRepoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git_repo);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewRepos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final GitRepoAdapter adapter = new GitRepoAdapter(this);
        recyclerView.setAdapter(adapter);

        gitRepoViewModel = ViewModelProviders.of(this).get(GitRepoViewModel.class);
        gitRepoViewModel.getAllRepos().observe(this, new Observer<Resource<GitHubResponse>>() {

            /*@Override
            public void onChanged(List<GitRepo> gitRepos) {
                adapter.submitList(gitRepos);
            }*/
        });

        adapter.setOnItemClickListener(new GitRepoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GitRepo gitRepo) {

            }
        });

        SwipeRefreshLayout swipeRefresher = findViewById(R.id.swipeRefreshId);
        swipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gitRepoViewModel.getAllRepos();
            }
        });
    }


}
