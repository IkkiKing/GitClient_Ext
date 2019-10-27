package com.ikkikingg.gitclient.GitRepo.Repository.Network;

import com.google.gson.annotations.SerializedName;

public class Owner {
    @SerializedName("login")
    private String login;
    @SerializedName("avatar_url")
    private String avatarUrl;

    public String getLogin() {
        return login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
