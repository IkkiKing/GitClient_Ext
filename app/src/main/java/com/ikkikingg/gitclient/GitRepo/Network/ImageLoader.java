package com.ikkikingg.gitclient.GitRepo.Network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.ikkikingg.gitclient.GitRepo.Model.GitRepo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImageLoader {

    private final List<GitRepo> gitRepos;
    private final Context context;
    private final Set<Target> protectedFromGarbageCollectorTargets = new HashSet<>();

    public ImageLoader(List<GitRepo> gitRepos, Context context) {
        this.gitRepos = gitRepos;
        this.context = context;
    }

    public void imageDownload() {

        for (GitRepo gitRepo : gitRepos) {

            BitmapTarget mTarget = new BitmapTarget(gitRepo.getOwner().getLogin());
            protectedFromGarbageCollectorTargets.add(mTarget);

            Picasso.get()
                    .load(gitRepo.getOwner().getAvatarUrl())
                    .into(mTarget);
        }
    }

    private class BitmapTarget implements Target {
        private final String login;

        public BitmapTarget(String login) {
            this.login = login;
        }

        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(context.getFilesDir().getPath() + "/" + login);
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            ostream.flush();
                            ostream.close();
                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                    protectedFromGarbageCollectorTargets.remove(this);
                }


            }).start();
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            protectedFromGarbageCollectorTargets.remove(this);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }
}
