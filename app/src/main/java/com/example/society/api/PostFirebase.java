package com.example.society.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.society.MainActivity;
import com.example.society.PostsFragment;
import com.example.society.adapters.PostAdapter;
import com.example.society.models.Post;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PostFirebase {

    final static String POST_COLLECTION = "posts";
    public static final String TAG = MainActivity.class.getName();

    public static void getAllPostsSince(long timeSince, final Post.Listener<List<Post>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts = new Timestamp(new Date(timeSince));
        db.collection(POST_COLLECTION)
                .whereGreaterThanOrEqualTo("lastUpdated", ts)
                .get()
                .addOnCompleteListener((new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Post> postsData;
                        if (task.isSuccessful()) {
                            postsData = new LinkedList<>();
                            for(QueryDocumentSnapshot doc : task.getResult()){
                                Map<String,Object> json = doc.getData();
                                Post post = postFromJson(json);
                                postsData.add(post);
                            }
                            listener.onComplete(postsData);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                }));
    }

    private static Post postFromJson(Map<String, Object> json){
        Post post = new Post();
        post.setPostId((String)json.get("postId"));
        post.setUserId((String)json.get("userId"));
        post.setAuthor((String)json.get("author"));
        post.setTitle((String)json.get("title"));
        post.setSubtitle((String)json.get("subtitle"));
        post.setDate((String)json.get("date"));
        post.setCover((String)json.get("cover"));
        post.setDeleted((boolean)json.get("deleted"));
        Timestamp ts = (Timestamp)json.get("lastUpdated");
        if (ts != null) post.setLastUpdated(ts.toDate().getTime());
        return post;
    }

    private static Map<String, Object> jsonFromPost(Post post) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("postId", post.getPostId());
        result.put("userId", post.getUserId());
        result.put("author", post.getAuthor());
        result.put("title", post.getTitle());
        result.put("subtitle", post.getSubtitle());
        result.put("date", post.getDate());
        result.put("cover", post.getCover());
        result.put("deleted", post.getDeleted());
        result.put("lastUpdated", FieldValue.serverTimestamp());
        return result;
    }

    public static void addPost(Post post, final Post.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(POST_COLLECTION)
                .document(post.getPostId())
                .set(jsonFromPost(post))
                .addOnCompleteListener((new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener != null) {
                    listener.onComplete(task.isSuccessful());
                }
            }
        }));
    }

    public static void updatePost(Post post, final PostAdapter.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(POST_COLLECTION)
                .document(post.getPostId())
                .set(post)
                .addOnCompleteListener((new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener != null) {
                    listener.onComplete(task.isSuccessful());
                }
            }
        }));
    }

    public static void deletePost(Post post) {
        Map<String,Object> updates = new HashMap<>();
        updates.put("lastUpdated", FieldValue.serverTimestamp());
        updates.put("deleted", true);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(POST_COLLECTION)
                .document(post.getPostId())
                .update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

}

//                .whereEqualTo("deleted", false)
//                not working - retrieving only 'deleted = false' docs means the device which didn't upload the post
//                but has it in cache won't override its previews 'deleted = false', so it won't delete it.























