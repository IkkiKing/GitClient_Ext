package com.ikkikingg.gitclient.GitRepo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ikkikingg.gitclient.GitRepo.Model.GitRepo;
import com.ikkikingg.gitclient.GitRepo.Model.GitRepoMatch;
import com.ikkikingg.gitclient.R;
import com.squareup.picasso.Picasso;

public class GitRepoAdapter extends ListAdapter<GitRepo, GitRepoAdapter.GitRepoHolder> {
    private Context context;
    private OnItemClickListener listener;

    public GitRepoAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<GitRepo> DIFF_CALLBACK = new DiffUtil.ItemCallback<GitRepo>() {
        @Override
        public boolean areItemsTheSame(@NonNull GitRepo oldItem, @NonNull GitRepo newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull GitRepo oldItem, @NonNull GitRepo newItem) {
            return new GitRepoMatch(oldItem, newItem).isMatch();
        }
    };

    @NonNull
    @Override
    public GitRepoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_repo, parent, false);
        return new GitRepoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GitRepoHolder holder, int position) {
        GitRepo gitRepo = getItem(position);

        holder.textViewRepoName.setText(gitRepo.getName());

        if (gitRepo.getLicense() != null) {
            holder.textViewLicense.setText(gitRepo.getLicense().getLicenseName());
        }
        if (gitRepo.getOwner() != null) {
            holder.textViewLogin.setText(gitRepo.getOwner().getLogin());
        }

        Picasso.get()
                .load(gitRepo.getOwner().getAvatarUrl())
                .placeholder(R.drawable.anim_loading_progress)
                .error(R.drawable.img_error_loading)
                .into(holder.imageViewAvatar);
    }


    class GitRepoHolder extends RecyclerView.ViewHolder {
        private TextView textViewRepoName;
        private TextView textViewLicense;
        private TextView textViewLogin;
        private ImageView imageViewAvatar;

        public GitRepoHolder(@NonNull View itemView) {
            super(itemView);
            textViewRepoName = itemView.findViewById(R.id.textViewRepoName);
            textViewLicense = itemView.findViewById(R.id.textViewLicense);
            textViewLogin = itemView.findViewById(R.id.textViewLogin);
            imageViewAvatar = itemView.findViewById(R.id.imageViewAvatar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }

    }

    public interface OnItemClickListener {
        void onItemClick(GitRepo gitRepo);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
