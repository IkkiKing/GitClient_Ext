package com.ikkikingg.gitclient.GitRepo.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "repo_table")
public class GitRepo {

    @PrimaryKey
    private int id;

    private String name;

    private String license;

    private String login;

    private String image;

    public GitRepo(int id, String name, String license, String login, String image) {
        this.id = id;
        this.name = name;
        this.license = license;
        this.login = login;
        this.image = image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLicense() {
        return license;
    }

    public String getLogin() {
        return login;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public void setOwner(String owner) {
        this.login = owner;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
