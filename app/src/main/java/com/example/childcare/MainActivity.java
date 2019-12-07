package com.example.childcare;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.request:
                attachFragment(new ParentConfirmationRequestFragment());

        }
        return false;
    }


    // methods which helps you to attach any fragment
    private void attachFragment(Fragment fragment) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (currentFragment == null || !currentFragment.getClass().getName().equals(fragment.getClass().getName())) {
            if (!isExistsInBackStack(fragment))
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(fragment.getClass().getName())
                        .replace(R.id.container, fragment)
                        .commit();
        }
    }

    private boolean isExistsInBackStack(Fragment fragment) {
        boolean isFound = false;
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            String fragmentName = getSupportFragmentManager().getBackStackEntryAt(i).getName();
            if (fragmentName.equals(fragment.getClass().getName()))
                isFound = true;
        }
        if (isFound) {
            getTransactionFromBackStack(fragment);
            return true;
        }
        return false;
    }

    private void getTransactionFromBackStack(Fragment fragment) {
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            String fragmentName = getSupportFragmentManager().getBackStackEntryAt(i).getName();
            if (fragmentName != null && !fragmentName.equals(fragment.getClass().getName())) {
                getSupportFragmentManager().popBackStackImmediate(i, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }
}
