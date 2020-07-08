package com.example.society.auth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.society.R;

public class SignInFragment extends Fragment {

    public interface Delegate {
        void registerClick();
        void onLoginClick(String email, String password);
    }

    public SignInFragment() {} // Required empty public constructor


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        final Delegate listener = (Delegate) getActivity();

        final TextView emailTextView = view.findViewById(R.id.fragment_signIn_textField_email);
        final TextView passwordTextView = view.findViewById(R.id.fragment_signIn_textField_password);
        Button registerBtn = view.findViewById(R.id.fragment_signIn_registerBtn);
        Button loginBtn = view.findViewById(R.id.fragment_signIn_loginBtn);
        final ProgressBar spinner = view.findViewById(R.id.fragment_signIn_spinner_progressBar);
        spinner.setVisibility(View.GONE);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.registerClick();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTextView.getText().toString();
                String password = passwordTextView.getText().toString();
                if (checkValidation(email, password)) {
                    spinner.setVisibility(View.VISIBLE);
                    listener.onLoginClick(email, password);
                } else {
                    Toast.makeText(getContext(), "Either the email or password format is invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private boolean checkValidation(String email, String password) {
        return (!email.isEmpty()
                && email.contains("@")
                && !password.isEmpty()
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

}
