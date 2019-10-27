package com.ikkikingg.gitclient.GitRepo.Repository.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitInstance {

    private static final String BASE_URL = "https://api.github.com/";

    private static Retrofit retrofit = null;

    public static GitHubRepoApi getApiService() {
        if (retrofit == null) {

            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit.create(GitHubRepoApi.class);

    }
}
