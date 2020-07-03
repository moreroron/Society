package com.example.society.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.society.R;
import com.example.society.models.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private List<Post> posts;

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
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

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.post_item_username_textView);
            date = itemView.findViewById(R.id.post_item_date_textView);
            subtitle = itemView.findViewById(R.id.post_item_subtitle_textView);
            title = itemView.findViewById(R.id.post_item_title_textView);
        }

        public void onBind(int position) {
            Post currentPost = posts.get(position);
            username.setText(currentPost.getAuthor());
            date.setText(currentPost.getDate());
            subtitle.setText(currentPost.getSubtitle());
            title.setText(currentPost.getTitle());
        }
    }

}
