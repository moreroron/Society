package com.example.society;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.society.api.PostFirebase;
import com.example.society.models.Post;
import com.example.society.repositories.PostRepository;
import com.example.society.utilities.Time;
import com.example.society.viewmodels.CreatePostViewModel;
import com.example.society.viewmodels.PostViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class CreatePostFragment extends Fragment {

    private Delegate parent;
    private CreatePostViewModel viewModel;

    public interface Delegate {
        void onSubmitPostClick();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CreatePostFragment.Delegate) {
            parent = (CreatePostFragment.Delegate) getActivity();
        } else {
            throw new RuntimeException(context.toString() + "CreatePostFragment's MainActivity must implement delegate methods");
        }

        viewModel = new ViewModelProvider(this).get(CreatePostViewModel.class);
    }

    public CreatePostFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);

        Button addPostBtn = view.findViewById(R.id.fragment_createPost_createPostBtn);
        final TextView titleTextView = view.findViewById(R.id.fragment_createPost_titleTextView);
        final TextView textTextView = view.findViewById(R.id.fragment_createPost_textTextView);

        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleTextView.getText().toString();
                String text = textTextView.getText().toString();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // for unique id
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String randomId = db.collection("posts").document().getId();

                String date = Calendar.getInstance().getTime().toString();

                Post post = new Post(randomId, user.getUid(), user.getDisplayName(), title, text, 0, date, "");
                viewModel.addPost(post);

                parent.onSubmitPostClick();
            }
        });

        return view;
    }
}
