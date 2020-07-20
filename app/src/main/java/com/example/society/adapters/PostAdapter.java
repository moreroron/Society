package com.example.society.adapters;

import android.net.Uri;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.society.R;
import com.example.society.models.Post;
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

    private FirebaseUser user;

    public interface Listener<T> {
        void onComplete(T data);
    }

    private List<Post> posts;



    public PostAdapter(List<Post> posts) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        this.posts = posts;
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
            Post currentPost = posts.get(position);
            username.setText(currentPost.getAuthor());
            CharSequence convertedTime = convertTimeTemplate(currentPost);
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

        }

        // from date to "x time ago template"
        public CharSequence convertTimeTemplate(Post post) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            Date date = null;
            try { date = sdf.parse(post.getDate()); }
            catch (Exception e){ e.printStackTrace(); }
            long postDate = date.getTime();
            CharSequence actualTime = DateUtils.getRelativeTimeSpanString(postDate, new Date().getTime(), MINUTE_IN_MILLIS);
            return actualTime;
        }
    }

}
