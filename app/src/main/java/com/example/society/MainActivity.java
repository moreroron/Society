package com.example.society;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.society.auth.SignInFragment;
import com.example.society.auth.SignInFragmentDirections;
import com.example.society.auth.SignUpFragment;
import com.example.society.auth.SignUpFragmentDirections;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity implements SignInFragment.Delegate, SignUpFragment.Delegate {

    private static final String TAG = "MAIN ACTIVITY";

    private NavController navController;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navController = Navigation.findNavController(this, R.id.activity_main_navHost);
        mAuth = FirebaseAuth.getInstance();
    }

    // redirect from signIn to SignUp
    @Override
    public void registerClick() {
        NavDirections direction = SignInFragmentDirections.actionSignInFragmentToSignUpFragment();
        navController.navigate(direction);
    }

    // login a user and direct forward
    @Override
    public void onLoginClick(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            NavDirections directions = SignInFragmentDirections.actionSignInFragmentToPostsFragment();
                            navController.navigate(directions);
                        } else {
                            Toast.makeText(MainActivity.this, "No such user in the system", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // direct forward if user logged in
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            NavDirections directions = SignInFragmentDirections.actionSignInFragmentToPostsFragment();
            navController.navigate(directions);
        }
    }

    // registers new user with email + password
    @Override
    public void onRegisterClick(String email, String password, final String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            addAdditionalInformation(username);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }

    // adds additional information for registered user (username + avatar) AND redirect forward
    private void addAdditionalInformation(String username) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                // TODO: set user avatar
                // .setPhotoUri(Uri.parse(avatar))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            NavDirections directions = SignUpFragmentDirections.actionSignUpFragmentToPostsFragment();
                            navController.navigate(directions);
                        }
                    }
                });
    }
}
