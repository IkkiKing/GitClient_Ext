package com.ikkikingg.gitclient.GitRepo.Network;

import com.google.gson.annotations.SerializedName;

public class License {

    @SerializedName("name")
    private String licenseName;

    public String getLicenseName() {
        return licenseName;
    }

    public void setLicenseName(String licenseName) {
        this.licenseName = licenseName;
    }
}
