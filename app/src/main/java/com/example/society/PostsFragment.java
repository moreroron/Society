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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.society.adapters.PostAdapter;
import com.example.society.models.Post;
import com.example.society.viewmodels.PostViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment {

    private List<Post> posts = new ArrayList<>();
    private Delegate parent;
    private PostViewModel viewModel;
    LiveData<List<Post>> postsLiveData;
    LiveData<Boolean> showSpinnerLiveData;
    private boolean showSpinner;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter postAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public interface Delegate {
        void onAddPostClick();
    }

    FloatingActionButton fab;
    ProgressBar spinner;

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

        recyclerViewConfig(view);

        fab = view.findViewById(R.id.fragment_posts_fab);

        spinner = view.findViewById(R.id.fragment_posts_spinner_progressBar);
        spinner.setVisibility(View.VISIBLE);

        postsLiveData = viewModel.getAllPosts();
//        showSpinnerLiveData = viewModel.showSpinner;
//        showSpinnerLiveData.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean postsArrived) {
//                    spinner.setVisibility(View.GONE);
//            }
//        });
        postsLiveData.observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> postsData) {
        // posts = postsData;
        // postAdapter.notifyDataSetChanged();
                postAdapter = new PostAdapter(postsData);
                recyclerView.setAdapter(postAdapter);
                spinner.setVisibility(View.GONE);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.onAddPostClick();
            }
        });

        return view;

    }

    private void recyclerViewConfig(View view) {
        recyclerView = view.findViewById(R.id.fragment_posts_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        postAdapter = new PostAdapter(posts);
        recyclerView.setAdapter(postAdapter);
    }

}
