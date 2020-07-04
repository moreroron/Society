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

import com.example.society.adapters.PostAdapter;
import com.example.society.models.Post;
import com.example.society.viewmodels.PostViewModel;
import com.example.society.viewmodels.ProfileViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private List<Post> posts = new ArrayList<>();
    private Delegate parent;
    private ProfileViewModel viewModel;
    LiveData<List<Post>> postsLiveData;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter postAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public interface Delegate {
        void onEditProfileClick();
    }

    public ProfileFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Delegate) {
            parent = (Delegate) getActivity();
        } else {
            throw new RuntimeException(context.toString() + "ProfileFragment's MainActivity must implement delegate methods");
        }
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        recyclerViewConfig(view);

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


        return view;
    }

    private void recyclerViewConfig(View view) {
        recyclerView = view.findViewById(R.id.fragment_profile_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        postAdapter = new PostAdapter(posts);
        recyclerView.setAdapter(postAdapter);
    }
}
