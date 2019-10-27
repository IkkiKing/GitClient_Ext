package com.ikkikingg.gitclient.GitRepo.Repository.Network;

import com.google.gson.annotations.SerializedName;

public class License {

    @SerializedName("name")
    private String name;

    public String getName(){
        return this.name;
    }
}
