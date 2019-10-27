package com.ikkikingg.gitclient.GitRepo.Repository;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.ikkikingg.gitclient.GitRepo.Model.GitRepoMatch;
import com.ikkikingg.gitclient.GitRepo.Repository.Database.GitRepoDao;
import com.ikkikingg.gitclient.GitRepo.Repository.Database.GitRepoDatabase;
import com.ikkikingg.gitclient.GitRepo.Model.GitRepo;
import com.ikkikingg.gitclient.GitRepo.Repository.Network.GitRepoNetwork;
import com.ikkikingg.gitclient.GitRepo.Repository.Network.OnNetworkReadyCallback;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GitRepoRepository {
    private GitRepoDao gitRepoDao;
    private GitRepoNetwork gitRepoNetwork;
    private LiveData<List<GitRepo>> allRepos;

    public GitRepoRepository(Application applicaiton) {

        GitRepoDatabase database = GitRepoDatabase.getInstance(applicaiton);
        gitRepoDao = database.gitRepoDao();

        if (isConnected(applicaiton)) {
            gitRepoNetwork = GitRepoNetwork.getInstance(applicaiton);
            allRepos = gitRepoNetwork.getAllRepos(new OnNetworkReadyCallback() {
                @Override
                public void onNetworkDataReady(List<GitRepo> gitRepos) {
                    if (gitRepos != null){
                        saveRepos(gitRepos);
                    }
                }
            });
        } else {
            allRepos = gitRepoDao.getAllRepos();
        }
    }

    private boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork.isConnectedOrConnecting();
    }


    //продумать проверку на существование записи в кэше перед сейвом
    public void saveRepos(List<GitRepo> gitRepos) {
        for (GitRepo gitRepo : gitRepos) {

            //Пытаемся найти в базе репозиторий с таким же ID
            GitRepo gitRepoDb = getGitRepoById(gitRepo.getId());

            if (gitRepoDb != null){
                ///Если нашли, проверяем что его содержиемое совпадает, если так сохранять его не нужно
                if (isRepoTheSame(gitRepoDb, gitRepo)){
                    continue;
                }else{
                //Если не совпадает удаляем и вставляем заново
                    delete(gitRepo);
                }
            }
            insert(gitRepo);

        }
    }

    private Boolean isRepoTheSame(GitRepo gitRepoDb, GitRepo gitRepo){
        return new GitRepoMatch(gitRepoDb, gitRepo).isMatch();
    }

    public void insert(GitRepo gitRepo) {
        new InsertGitRepoAsyncTask(gitRepoDao).execute(gitRepo);
    }


    public void update(GitRepo gitRepo) {
        new UpdatetGitRepoAsyncTask(gitRepoDao).execute(gitRepo);
    }

    public void delete(GitRepo gitRepo) {
        new DeleteGitRepoAsyncTask(gitRepoDao).execute(gitRepo);
    }

    public void deleteAll() {
        new DeleteAllGitRepoAsyncTask(gitRepoDao).execute();
    }

    public LiveData<List<GitRepo>> getAllRepos() {
        return allRepos;
    }

    private GitRepo getGitRepoById(int id) {
        GitRepo gitRepo = null;
        AsyncTask asyncTask =  new SelectGitRepoAsyncTask(gitRepoDao).execute(new Integer(id));
        try {
            gitRepo = (GitRepo) asyncTask.get();
        } catch (InterruptedException e) {
            Log.e("GET_BY_ID" , "InterruptedException trying to select by id = " + id);
        } catch (ExecutionException e) {
            Log.e("GET_BY_ID" , "ExecutionException trying to select by id =" + id);
        }
        return gitRepo;
    }

    private static class SelectGitRepoAsyncTask extends AsyncTask<Integer, Void, GitRepo> {
        private GitRepoDao gitRepoDao;

        private SelectGitRepoAsyncTask(GitRepoDao gitRepoDao) {
            this.gitRepoDao = gitRepoDao;
        }

        @Override
        protected GitRepo doInBackground(Integer... integers) {
            int value = integers[0].intValue();
            return gitRepoDao.getGitRepoById(value);
        }

        @Override
        protected void onPostExecute(GitRepo gitRepo) {
            super.onPostExecute(gitRepo);
        }
    }

    private static class InsertGitRepoAsyncTask extends AsyncTask<GitRepo, Void, Void> {
        private GitRepoDao gitRepoDao;

        private InsertGitRepoAsyncTask(GitRepoDao gitRepoDao) {
            this.gitRepoDao = gitRepoDao;
        }

        @Override
        protected Void doInBackground(GitRepo... gitRepos) {
            gitRepoDao.insert(gitRepos[0]);
            return null;
        }
    }


    private static class UpdatetGitRepoAsyncTask extends AsyncTask<GitRepo, Void, Void> {
        private GitRepoDao gitRepoDao;

        private UpdatetGitRepoAsyncTask(GitRepoDao gitRepoDao) {
            this.gitRepoDao = gitRepoDao;
        }

        @Override
        protected Void doInBackground(GitRepo... gitRepos) {
            gitRepoDao.update(gitRepos[0]);
            return null;
        }
    }

    private static class DeleteGitRepoAsyncTask extends AsyncTask<GitRepo, Void, Void> {
        private GitRepoDao gitRepoDao;

        private DeleteGitRepoAsyncTask(GitRepoDao gitRepoDao) {
            this.gitRepoDao = gitRepoDao;
        }

        @Override
        protected Void doInBackground(GitRepo... gitRepos) {
            gitRepoDao.delete(gitRepos[0]);
            return null;
        }
    }

    private static class DeleteAllGitRepoAsyncTask extends AsyncTask<Void, Void, Void> {
        private GitRepoDao gitRepoDao;

        private DeleteAllGitRepoAsyncTask(GitRepoDao gitRepoDao) {
            this.gitRepoDao = gitRepoDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            gitRepoDao.deleteAllRepos();
            return null;
        }
    }


}
