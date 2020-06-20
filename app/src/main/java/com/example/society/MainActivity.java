package com.example.society;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements SignInFragment.Delegate {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this, R.id.main_navHost);
    }

    @Override
    public void registerClick() {
        NavDirections direction = SignInFragmentDirections.actionSignInFragmentToSignUpFragment();
        navController.navigate(direction);
    }
}
