package com.ikkikingg.gitclient.GitRepoDetail.Network;

import com.google.gson.annotations.SerializedName;

public class Owner {

    @SerializedName("login")
    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
