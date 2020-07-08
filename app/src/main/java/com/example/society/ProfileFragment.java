package com.example.society;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.society.adapters.PostAdapter;
import com.example.society.adapters.ProfileAdapter;
import com.example.society.models.Post;
import com.example.society.viewmodels.PostViewModel;
import com.example.society.viewmodels.ProfileViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements ProfileAdapter.AdapterCallback {

    private List<Post> posts = new ArrayList<>();
    private ProfileViewModel viewModel;
    LiveData<List<Post>> postsLiveData;

    View view;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter profileAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private TextView username;
    private ImageView avatar;

    public ProfileFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        username = view.findViewById(R.id.fragment_profile_username_TextView);
        avatar = view.findViewById(R.id.fragment_profile_avatar_imageView);

        recyclerViewConfig(view);

        postsLiveData = viewModel.getPosts();
        postsLiveData.observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> postsData) {
                profileAdapter = new ProfileAdapter(postsData, getActivity() ,ProfileFragment.this);
                recyclerView.setAdapter(profileAdapter);
            }
        });

        if (user != null) {
            username.setText(user.getDisplayName());
        }

        return view;
    }

    private void recyclerViewConfig(View view) {
        recyclerView = view.findViewById(R.id.fragment_profile_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        profileAdapter = new ProfileAdapter(posts, getActivity(), ProfileFragment.this);
        recyclerView.setAdapter(profileAdapter);
        
    }

    @Override
    public void onEditClick(String postId) {
        ProfileFragmentDirections.ActionProfileFragmentToEditPostFragment action = ProfileFragmentDirections.actionProfileFragmentToEditPostFragment(null);
        action.setPostId(postId);
        Navigation.findNavController(view).navigate(action);
    }

    @Override
    public void onDeleteClick(Post post) {
        viewModel.deletePost(post);
    }
}
