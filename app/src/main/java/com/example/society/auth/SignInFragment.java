package com.example.society.auth;

import android.os.Bundle;

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

import com.example.society.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

public class SignInFragment extends Fragment implements Validator.ValidationListener {

    private Validator validator;
    private boolean validForm;
    private Button registerBtn;
    private Button loginBtn;
    private ProgressBar spinner;

    @NotEmpty
    @Email
    private TextView emailTextView;

    @NotEmpty
    @Password(min = 6)
    private TextView passwordTextView;
    private Delegate listener;

    public interface Delegate {
        void registerClick();
        void onLoginClick(String email, String password, AuthActivity.Listener listener);
    }

    public SignInFragment() {} // Required empty public constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        initView(view);
        validator = new Validator(this);
        validator.setValidationListener(this);
        return view;
    }

    public void initView(View view) {
        validForm = false;
        listener = (Delegate) getActivity();
        emailTextView = view.findViewById(R.id.fragment_signIn_textField_email);
        passwordTextView = view.findViewById(R.id.fragment_signIn_textField_password);
        registerBtn = view.findViewById(R.id.fragment_signIn_registerBtn);
        loginBtn = view.findViewById(R.id.fragment_signIn_loginBtn);
        spinner = view.findViewById(R.id.fragment_signIn_spinner_progressBar);
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
                login();
            }
        });
    }

    private void login() {
        validator.validate();
        String email = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();
        if (validForm) {
            spinner.setVisibility(View.VISIBLE);
            listener.onLoginClick(email, password, new AuthActivity.Listener() {
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
