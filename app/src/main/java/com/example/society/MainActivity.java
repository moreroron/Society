package com.example.society;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.society.auth.AuthActivity;
import com.example.society.auth.SignInFragment;
import com.example.society.auth.SignInFragmentDirections;
import com.example.society.auth.SignUpFragment;
import com.example.society.auth.SignUpFragmentDirections;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity implements
        PostsFragment.Delegate,
        CreatePostFragment.Delegate,
        ProfileFragment.Delegate,
        NavigationView.OnNavigationItemSelectedListener
{

    private static final String TAG = "MAIN ACTIVITY";

    private NavController navController;
    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        navController = Navigation.findNavController(this, R.id.activity_main_navHost);
        drawerConfig();

    }

    private void drawerConfig() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.drawer_menu_profile: {
                NavDirections directions = PostsFragmentDirections.actionGlobalProfileFragment();
                navController.navigate(directions);
                break;
            }
            case R.id.drawer_menu_signout: {
                FirebaseAuth.getInstance().signOut();
                ActivityNavigator activityNavigator = new ActivityNavigator(getApplicationContext());
                activityNavigator.navigate(activityNavigator.createDestination()
                        .setIntent(new Intent(getApplicationContext(), AuthActivity.class)), null, null, null);
                break;
            }
        }
        menuItem.setChecked(true);
        NavigationUI.onNavDestinationSelected(menuItem, navController);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
                else {
                    return false;
                }
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAddPostClick() {
        NavDirections directions = PostsFragmentDirections.actionPostsFragmentToCreatePostFragment();
        navController.navigate(directions);
    }

    @Override
    public void onSubmitPostClick() {
        NavDirections directions = CreatePostFragmentDirections.actionGlobalPostsFragment();
        navController.navigate(directions);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.activity_main_navHost), drawerLayout);
    }

    @Override
    public void onEditProfileClick() {

    }
}
