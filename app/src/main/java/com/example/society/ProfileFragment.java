package com.example.society;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.society.adapters.ProfileAdapter;
import com.example.society.api.UserFirebase;
import com.example.society.models.Post;
import com.example.society.models.StoreModel;
import com.example.society.models.User;
import com.example.society.utilities.Dates;
import com.example.society.viewmodels.ProfileViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements ProfileAdapter.AdapterCallback {

    public static final String TAG = MainActivity.class.getName();
    static final int RESULT_LOAD_IMAGE = 1;

    private List<Post> posts = new ArrayList<>();
    private ProfileViewModel viewModel;
    LiveData<List<Post>> postsLiveData;

    View view;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter profileAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private TextView username;
    private ImageView avatar;
    private FloatingActionButton changeAvatar;
    private Bitmap avatarBitmap;

    private FirebaseUser user;
    private ProgressBar spinner;
    private View whitescreen;

    public ProfileFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        username = view.findViewById(R.id.fragment_profile_username_TextView);
        avatar = view.findViewById(R.id.fragment_profile_avatar_imageView);
        changeAvatar = view.findViewById(R.id.fragment_profile_changeAvatar_floatingActionBtn);
        spinner = view.findViewById(R.id.fragment_profile_spinner_progressBar);
        whitescreen = view.findViewById(R.id.fragment_profile_whitescreen_view);

        recyclerViewConfig(view);

        postsLiveData = viewModel.getPosts();
        postsLiveData.observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(List<Post> postsData) {
                List<Post> sortedPosts = Dates.sortPosts(postsData);
                profileAdapter = new ProfileAdapter(sortedPosts ,ProfileFragment.this);
                recyclerView.setAdapter(profileAdapter);
            }
        });

        if (user != null) {
            username.setText(user.getDisplayName());
            Picasso.get().load(user.getPhotoUrl()).placeholder(R.mipmap.ic_launcher).into(avatar);
        }

        // CHANGE AVATAR
        changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        return view;
    }

    private void recyclerViewConfig(View view) {
        recyclerView = view.findViewById(R.id.fragment_profile_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        profileAdapter = new ProfileAdapter(posts, ProfileFragment.this);
        recyclerView.setAdapter(profileAdapter);
    }

    @Override
    public void onEditClick(String postId) {
        ProfileFragmentDirections.ActionProfileFragmentToEditPostFragment action = ProfileFragmentDirections.actionProfileFragmentToEditPostFragment(null);
        action.setPostId(postId);
        Navigation.findNavController(view).navigate(action);
    }

    @Override
    public void onDeleteClick(final Post post) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Delete Post");
        alertDialogBuilder.setMessage("Are you sure you want do delete " + "\"" + post.getTitle() + "\"" + "?\nThere is no turning back.");
        alertDialogBuilder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.deletePost(post);
            }
        });
        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        alertDialogBuilder.create();
        alertDialogBuilder.show();
    }

    private void takePhoto() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    Uri imageUri = data.getData();
                    InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                    avatarBitmap = BitmapFactory.decodeStream(imageStream);
                    avatar.setImageBitmap(avatarBitmap);

                    // UPLOAD AVATAR TO FIREBASE STORAGE
                    uploadToFirebaseStorage();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void uploadToFirebaseStorage() {
        spinner.setVisibility(View.VISIBLE);
        whitescreen.setVisibility(View.VISIBLE);
        StoreModel.saveImage(avatarBitmap, user.getUid(), new StoreModel.Listener() {
            @Override
            public void onSuccess(String url) {
                // UPDATE USER'S AVATAR
                UserFirebase.updateUserAvatar(url, new User.Listener<Boolean>() {
                    @Override
                    public void onComplete(Boolean bool) {
                        spinner.setVisibility(View.GONE);
                        whitescreen.setVisibility(View.GONE);
                        recyclerView.setAdapter(profileAdapter);
                        Toast.makeText(getContext(), "Avatar Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onFail() {
                spinner.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Failed to Update Avatar", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
