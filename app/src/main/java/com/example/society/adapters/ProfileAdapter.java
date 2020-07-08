package com.example.society.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.society.R;
import com.example.society.models.Post;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.PostHolder>  {

    private AdapterCallback callback;

    public interface AdapterCallback {
        void onEditClick(String postId);
        void onDeleteClick(Post post);
    }

    private List<Post> posts;

    public ProfileAdapter(List<Post> posts, Context context, AdapterCallback callback) {
        this.posts = posts;

        try {
            this.callback = callback;
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment must implement AdapterCallback.");
        }
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_profile, parent, false);
        return new PostHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        ((PostHolder) holder).onBind(position);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class PostHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private TextView date;
        private TextView subtitle;
        private TextView title;
        private ImageView editBtn;
        private ImageView deleteBtn;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.post_item_profile_username_textView);
            date = itemView.findViewById(R.id.post_item_profile_date_textView);
            subtitle = itemView.findViewById(R.id.post_item_profile_subtitle_textView);
            title = itemView.findViewById(R.id.post_item_profile_title_textView);
            editBtn = itemView.findViewById(R.id.post_item_profile_editBtn_Btn);
            deleteBtn = itemView.findViewById(R.id.post_item_profile_deleteBtn_Btn);
        }

        public void onBind(int position) {
            final Post currentPost = posts.get(position);
            username.setText(currentPost.getAuthor());
            date.setText(currentPost.getDate());
            subtitle.setText(currentPost.getSubtitle());
            title.setText(currentPost.getTitle());
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            callback.onDeleteClick(currentPost);
                        }
                    }
                }
            });
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            callback.onEditClick(currentPost.getPostId());
                        }
                    }
                }
            });
        }
    }

}

