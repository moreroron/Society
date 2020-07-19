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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.society.CreatePostFragment;
import com.example.society.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

public class SignUpFragment extends Fragment implements Validator.ValidationListener {

    private ProgressBar spinner;

    public interface Delegate {
        void onRegisterClick(String email, String password, String username, AuthActivity.Listener listener);
        void onBackToLoginClick();
    }

    private Validator validator;
    private boolean validForm;

    private Delegate parent;

    @NotEmpty
    @Email
    private TextView emailTextView;

    @NotEmpty
    @Password(min = 6)
    private TextView passwordTextView;

    @NotEmpty
    @Length(min = 3)
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
        initView(view);
        validator = new Validator(this);
        validator.setValidationListener(this);
        return view;
    }

    private void initView(View view) {
        emailTextView = view.findViewById(R.id.fragment_signIn_textField_email);
        passwordTextView = view.findViewById(R.id.fragment_signIn_textField_password);
        usernameTextView = view.findViewById(R.id.signUp_textField_username);
        registerBtn = view.findViewById(R.id.fragment_signIn_loginBtn);
        loginBtn = view.findViewById(R.id.fragment_signIn_registerBtn);
        spinner = view.findViewById(R.id.fragment_signUp_spinner_progressBar);
        spinner.setVisibility(View.GONE);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.onBackToLoginClick();
            }
        });
    }

    private void register() {
        validator.validate();
        String emailValue = emailTextView.getText().toString();
        String passwordValue = passwordTextView.getText().toString();
        String usernameValue = usernameTextView.getText().toString();

        if (validForm) {
            spinner.setVisibility(View.VISIBLE);
            parent.onRegisterClick(emailValue, passwordValue, usernameValue, new AuthActivity.Listener() {
                @Override
                public void onComplete() {
                    spinner.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
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
