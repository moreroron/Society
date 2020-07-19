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
import com.example.society.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.PostHolder>  {

    private User currentUser;
    private FirebaseUser user;
    private List<Post> posts;

    private AdapterCallback callback;

    public interface AdapterCallback {
        void onEditClick(String postId);
        void onDeleteClick(Post post);
    }

    public ProfileAdapter(List<Post> posts, AdapterCallback callback) {
        user = FirebaseAuth.getInstance().getCurrentUser();
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
        private ImageView avatar;
        private ImageView editBtn;
        private ImageView deleteBtn;
        private ImageView cover;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.post_item_profile_username_textView);
            date = itemView.findViewById(R.id.post_item_profile_date_textView);
            subtitle = itemView.findViewById(R.id.post_item_profile_subtitle_textView);
            title = itemView.findViewById(R.id.post_item_profile_title_textView);
            avatar = itemView.findViewById(R.id.post_item_profile_avatar_imageView);
            cover = itemView.findViewById(R.id.post_item_profile_cover_imageView);
            editBtn = itemView.findViewById(R.id.post_item_profile_editBtn_Btn);
            deleteBtn = itemView.findViewById(R.id.post_item_profile_deleteBtn_Btn);
        }

        public void onBind(int position) {
            final Post currentPost = posts.get(position);
            username.setText(currentPost.getAuthor());
            date.setText(currentPost.getDate());
            subtitle.setText(currentPost.getSubtitle());
            title.setText(currentPost.getTitle());
            Picasso.get().load(currentPost.getCover()).placeholder(R.mipmap.ic_launcher).into(cover);

            if (user != null) {
                Picasso.get().load(user.getPhotoUrl()).placeholder(R.mipmap.ic_launcher).into(avatar);
            }

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

