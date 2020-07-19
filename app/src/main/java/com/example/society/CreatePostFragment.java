package com.example.society;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.society.api.UserFirebase;
import com.example.society.models.Post;
import com.example.society.models.StoreModel;
import com.example.society.viewmodels.CreatePostViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class CreatePostFragment extends Fragment implements Validator.ValidationListener {

    public static final String TAG = MainActivity.class.getName();
    static final int RESULT_LOAD_IMAGE = 1;

    private Delegate parent;
    private CreatePostViewModel viewModel;
    private String randomPostId;
    private String coverUrl;
    private boolean validForm;

    private Bitmap coverBitmap;
    private ImageButton cover;
    private ProgressBar spinner;
    private Button addPostBtn;

    private Validator validator;

    @NotEmpty
    @Length(min = 3)
    private TextView titleTextView;

    @NotEmpty
    @Length(min = 3)
    private TextView textTextView;
    private String dateTime;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);
        initView(view);
        validator = new Validator(this);
        validator.setValidationListener(this);
        return view;
    }

    private void initView(View view) {
        validForm = false;
        spinner = view.findViewById(R.id.fragment_createPost_spinner_progressBar);
        spinner.setVisibility(View.GONE);
        titleTextView = view.findViewById(R.id.fragment_createPost_titleTextView);
        textTextView = view.findViewById(R.id.fragment_createPost_textTextView);
        cover = view.findViewById(R.id.fragment_createPost_cover_imageButton);
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        addPostBtn = view.findViewById(R.id.fragment_createPost_createPostBtn);
        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();
            }
        });
    }

    private void addPost() {
        validator.validate();
        String title = titleTextView.getText().toString();
        String text = textTextView.getText().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (coverUrl != null && validForm) {
            spinner.setVisibility(View.GONE);
            Post post = new Post(randomPostId, user.getUid(), user.getDisplayName(), title, text, dateTime, coverUrl, false);
            viewModel.addPost(post);
            parent.onSubmitPostClick();
        }
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
                    coverBitmap = BitmapFactory.decodeStream(imageStream);
                    cover.setImageBitmap(coverBitmap);

                    // UPLOAD COVER TO FIREBASE STORAGE
                    uploadToFirebaseStorage();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void uploadToFirebaseStorage() {
        spinner.setVisibility(View.VISIBLE);
        addPostBtn.setEnabled(false);

        // for unique id
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        randomPostId = db.collection("posts").document().getId();
        dateTime = Calendar.getInstance().getTime().toString();
        StoreModel.saveImage(coverBitmap, randomPostId + "_" + dateTime, new StoreModel.Listener() {
            @Override
            public void onSuccess(String url) {
                spinner.setVisibility(View.GONE);
                coverUrl = url;
                addPostBtn.setEnabled(true);
            }
            @Override
            public void onFail() {
                Log.d(TAG, "Fail to save avatar to FB local storage");
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        validForm = true;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        validForm = false;
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
