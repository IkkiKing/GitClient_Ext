package com.ikkikingg.gitclient.GitRepo.Model;

public class GitRepoMatch {
    private GitRepo gitRepoOld;
    private GitRepo gitRepoNew;

    public GitRepoMatch(GitRepo gitRepoOld,
                        GitRepo gitRepoNew) {
        this.gitRepoOld = gitRepoOld;
        this.gitRepoNew = gitRepoNew;
    }

    public Boolean isMatch() {
        return gitRepoOld.getLogin().equals(gitRepoNew.getLogin()) &&
                gitRepoOld.getName().equals(gitRepoNew.getName()) &&
                gitRepoOld.getImage().equals(gitRepoNew.getImage()) &&
                gitRepoOld.getLicense().equals(gitRepoNew.getLicense()) &&
                gitRepoOld.getId() == gitRepoNew.getId();
    }

}
