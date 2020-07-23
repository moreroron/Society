package com.example.society;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.society.adapters.PostAdapter;
import com.example.society.adapters.ProfileAdapter;
import com.example.society.models.Post;
import com.example.society.utilities.Dates;
import com.example.society.viewmodels.ProfileViewModel;
import com.example.society.viewmodels.UserDetailsViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsFragment extends Fragment {

    private List<Post> posts = new ArrayList<>();
    private UserDetailsViewModel viewModel;
    LiveData<List<Post>> postsLiveData;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PostAdapter postAdapter;

    public UserDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(UserDetailsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);

        recyclerViewConfig(view);

        String userId = UserDetailsFragmentArgs.fromBundle(getArguments()).getUserId();

        postsLiveData = viewModel.getPosts(userId);
        postsLiveData.observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(List<Post> postsData) {
                List<Post> sortedPosts = Dates.sortPosts(postsData);
                postAdapter = new PostAdapter(sortedPosts);
                recyclerView.setAdapter(postAdapter);
            }
        });

        return view;
    }

    private void recyclerViewConfig(View view) {
        recyclerView = view.findViewById(R.id.fragment_userDetails_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        postAdapter = new PostAdapter(posts);
        recyclerView.setAdapter(postAdapter);
    }
}