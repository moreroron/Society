package com.example.society.auth;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.society.CreatePostFragment;
import com.example.society.R;

public class SignUpFragment extends Fragment {

    public interface Delegate {
        void onRegisterClick(String email, String password, String username);
        void onBackToLoginClick();
    }

    private Delegate parent;

    private TextView emailTextView;
    private TextView passwordTextView;
    private TextView usernameTextView;
    private Button registerBtn;
    private Button loginBtn;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof SignUpFragment.Delegate) {
            parent = (SignUpFragment.Delegate) getActivity();
        } else {
            throw new RuntimeException(context.toString() + "SignUpFragment's AuthActivity must implement delegate methods");
        }
    }

    public SignUpFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        emailTextView = view.findViewById(R.id.fragment_signIn_textField_email);
        passwordTextView = view.findViewById(R.id.fragment_signIn_textField_password);
        usernameTextView = view.findViewById(R.id.signUp_textField_username);
        registerBtn = view.findViewById(R.id.fragment_signIn_loginBtn);
        loginBtn = view.findViewById(R.id.fragment_signIn_registerBtn);
        final ProgressBar spinner = view.findViewById(R.id.fragment_signUp_spinner_progressBar);
        spinner.setVisibility(View.GONE);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailValue = emailTextView.getText().toString();
                String passwordValue = passwordTextView.getText().toString();
                String usernameValue = usernameTextView.getText().toString();

                if (checkValidation(emailValue, passwordValue, usernameValue)) {
                    spinner.setVisibility(View.VISIBLE);
                    parent.onRegisterClick(emailValue, passwordValue, usernameValue);
                } else {
                    Toast.makeText(getContext(), "Either the password, username or email format is invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.onBackToLoginClick();
            }
        });

        return view;
    }

    private boolean checkValidation(String email, String password, String username) {
        return (!email.isEmpty()
                && email.contains("@")
                && !password.isEmpty()
                && !username.isEmpty()
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

}
