package com.ikkikingg.gitclient.GitRepo.Model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.ikkikingg.gitclient.GitRepo.Network.License;
import com.ikkikingg.gitclient.GitRepo.Network.Owner;

@Entity
public class GitRepo {

    @PrimaryKey
    private int id;

    @SerializedName("name")
    private String repoName;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("description")
    private String description;

    @Embedded
    private Owner owner;

    @Embedded
    private License license;

    public int getId() {
        return id;
    }

    public String getName() {
        return repoName;
    }

    public String getFullName() {
        return fullName;
    }

    public Owner getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }


    public License getLicense() {
        return license;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }
}
