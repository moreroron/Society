package com.example.society.adapters;

import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.society.PostsFragment;
import com.example.society.PostsFragmentDirections;
import com.example.society.R;
import com.example.society.UserDetailsFragmentArgs;
import com.example.society.UserDetailsFragmentDirections;
import com.example.society.api.PostFirebase;
import com.example.society.models.Post;
import com.example.society.utilities.Dates;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE;
import static android.text.format.DateUtils.MINUTE_IN_MILLIS;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private List<Post> posts;
    private AvatarClick callback;

    public interface Listener<T> {
        void onComplete(T data);
    }

    public interface AvatarClick {
        void avatarOnClick(Post post, View view);
    }

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    public void setAvatarClickListener(AvatarClick listener) {
        this.callback = listener;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        (holder).onBind(position);
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
        private ImageView cover;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.post_item_username_textView);
            date = itemView.findViewById(R.id.post_item_date_textView);
            subtitle = itemView.findViewById(R.id.post_item_subtitle_textView);
            title = itemView.findViewById(R.id.post_item_title_textView);
            avatar = itemView.findViewById(R.id.post_item_avatar_imageView);
            cover = itemView.findViewById(R.id.post_item_cover_imageView);
        }

        public void onBind(int position) {
            final Post currentPost = posts.get(position);
            username.setText(currentPost.getAuthor());
            CharSequence convertedTime = Dates.convertTimeTemplate(currentPost);
            date.setText(convertedTime);
            subtitle.setText(currentPost.getSubtitle());
            title.setText(currentPost.getTitle());

            Picasso.get().load(currentPost.getCover()).placeholder(R.mipmap.ic_launcher).into(cover);

            // get avatar from local storage by Post's User ID
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            storageRef.child("images/" + currentPost.getUserId())
                    .getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).placeholder(R.mipmap.ic_launcher).into(avatar);
                }
            });

            avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // check if we are in PostFragment - if so, avatar is clickable and directs to user's details
                    if (callback instanceof PostsFragment)
                    callback.avatarOnClick(currentPost, v);
                }
            });

        }
    }

}
