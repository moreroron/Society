package com.example.society;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.society.adapters.PostAdapter;
import com.example.society.models.Post;
import com.example.society.viewmodels.PostViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment {

    private List<Post> posts = new ArrayList<>();
    private Delegate parent;
    private PostViewModel viewModel;
    LiveData<List<Post>> postsLiveData;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter postAdapter;
    private RecyclerView.LayoutManager layoutManager;


    public interface Delegate {
        void onAddPostClick();
    }

    TextView username;
    Button addPostBtn;

    public PostsFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Delegate) {
            parent = (Delegate) getActivity();
        } else {
            throw new RuntimeException(context.toString() + "PostFragment's MainActivity must implement delegate methods");
        }
        viewModel = new ViewModelProvider(this).get(PostViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        // TODO: move to separate function
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_posts_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        postAdapter = new PostAdapter(posts);
        recyclerView.setAdapter(postAdapter);

        username = view.findViewById(R.id.fragment_posts_textView_username);
        addPostBtn = view.findViewById(R.id.fragment_posts_addPostBtn);

        postsLiveData = viewModel.getPosts();
        postsLiveData.observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> postsData) {
//                posts = postsData;
//                postAdapter.notifyDataSetChanged();
                postAdapter = new PostAdapter(postsData);
                recyclerView.setAdapter(postAdapter);
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String _username = user.getDisplayName();
            username.setText(_username);
        }

        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.onAddPostClick();
            }
        });

        return view;

    }
}
