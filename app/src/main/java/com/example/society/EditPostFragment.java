package com.example.society;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.society.models.Post;
import com.example.society.viewmodels.EditPostViewModel;
import com.example.society.viewmodels.PostViewModel;

import java.util.List;

public class EditPostFragment extends Fragment {

    EditPostViewModel viewModel;
    LiveData<Post> postLiveData;
    private Post post;
    private TextView title;
    private TextView text;
    private Button submitChangesBtn;

    public EditPostFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(EditPostViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_edit_post, container, false);

        title = view.findViewById(R.id.fragment_editPost_title_textView);
        text = view.findViewById(R.id.fragment_editPost_text_textView);
        submitChangesBtn = view.findViewById(R.id.fragment_editPost_editPost_btn);

        String postId = EditPostFragmentArgs.fromBundle(getArguments()).getPostId();
        postLiveData = viewModel.getPost(postId);
        postLiveData.observe(getViewLifecycleOwner(), new Observer<Post>() {
            @Override
            public void onChanged(Post updatedPost) {
                post = updatedPost;
                title.setText(updatedPost.getTitle());
                text.setText(updatedPost.getSubtitle());
            }
        });

        if (post != null) {
            title.setText(post.getTitle());
            text.setText(post.getSubtitle());
        }

        submitChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (post != null) {
                    post.setTitle(title.getText().toString());
                    post.setSubtitle(text.getText().toString());
                    viewModel.updatePost(post);

                    NavDirections directions = EditPostFragmentDirections.actionEditPostFragmentToProfileFragment();
                    Navigation.findNavController(view).navigate(directions);
                }
            }
        });

        return view;
    }


}