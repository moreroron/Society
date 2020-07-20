package com.example.society.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.society.MainActivity;
import com.example.society.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class AuthActivity extends AppCompatActivity implements
        SignInFragment.Delegate,
        SignUpFragment.Delegate
{

    public interface Listener {
        void onComplete();
    }

    private static final String TAG = "AUTH ACTIVITY";
    private FirebaseAuth mAuth;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        navController = Navigation.findNavController(this, R.id.activity_auth_navHost);

        mAuth = FirebaseAuth.getInstance();
    }

    // direct forward if user logged in
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            ActivityNavigator activityNavigator = new ActivityNavigator(this);
            activityNavigator.navigate(activityNavigator.createDestination()
                    .setIntent(new Intent(this, MainActivity.class)), null, null, null);
        }
    }

    // redirect from signIn to SignUp
    @Override
    public void registerClick() {
        NavDirections direction = SignInFragmentDirections.actionSignInFragmentToSignUpFragment();
        navController.navigate(direction);
    }

    // login a user and direct forward
    @Override
    public void onLoginClick(String email, String password, final Listener listener) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        listener.onComplete();
                        if (task.isSuccessful()) {
                            ActivityNavigator activityNavigator = new ActivityNavigator(getApplicationContext());
                            activityNavigator.navigate(activityNavigator.createDestination()
                                    .setIntent(new Intent(getApplicationContext(), MainActivity.class)), null, null, null);
                        } else {
                            Toast.makeText(AuthActivity.this, "No such user in the system", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackToLoginClick() {
        NavDirections directions = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment();
        navController.navigate(directions);
    }

    // registers new user with email + password
    @Override
    public void onRegisterClick(String email, String password, final String username, final Listener listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            addAdditionalInformation(username, listener);
                        } else {
                            listener.onComplete();
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                Toast.makeText(AuthActivity.this, "Weak password", Toast.LENGTH_SHORT).show();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(AuthActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                            } catch(FirebaseAuthUserCollisionException e) {
                                Toast.makeText(AuthActivity.this, "This user is already taken.", Toast.LENGTH_SHORT).show();
                            } catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }

    // adds additional information for registered user (USERNAME) + redirect forward
    private void addAdditionalInformation(String username, final Listener listener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            listener.onComplete();
                            ActivityNavigator activityNavigator = new ActivityNavigator(getApplicationContext());
                            activityNavigator.navigate(activityNavigator.createDestination()
                                    .setIntent(new Intent(getApplicationContext(), MainActivity.class)), null, null, null);
                        }
                    }
                });
    }
}
