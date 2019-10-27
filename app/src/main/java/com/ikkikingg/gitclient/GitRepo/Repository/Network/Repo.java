package com.ikkikingg.gitclient.GitRepo.Repository.Network;

import com.google.gson.annotations.SerializedName;

public class Repo {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("owner")
    private Owner owner;
    @SerializedName("description")
    private String description;
    @SerializedName("watchers_count")
    private int watchersCount;
    @SerializedName("forks_count")
    private int forksCount;
    @SerializedName("license")
    private License license;
    @SerializedName("stargazers_count")
    private int stargazersCoune;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public int getWatchersCount() {
        return watchersCount;
    }

    public int getForksCount() {
        return forksCount;
    }

    public License getLicense() {
        return license;
    }

    public int getStargazersCoune() {
        return stargazersCoune;
    }
}
