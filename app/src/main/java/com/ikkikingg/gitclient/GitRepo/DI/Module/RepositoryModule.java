package com.ikkikingg.gitclient.GitRepo.DI.Module;

import android.content.Context;

import java.util.concurrent.Executor;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {
    final private Context context;

    public RepositoryModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public Context provideContext(){
        return context;
    }
}
