package com.example.childcare;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
      //  if(userIsChild()) {
           MenuItem map = navigationView.findViewById(R.id.map);
           map.setVisible(false);
        //    createDialog();
        //}

    }

    private boolean userIsChild() {
        FirebaseDatabase.getInstance().getReference("Users").child("ChildUsers");
        // todo check if the user is Child to display a child homeActivity
        return true;

    }

    private boolean isParentIdValid(String parentId) {

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Users").child("ParentUsers");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                if (snapshot.hasChild(parentId)) {
                    
                    FirebaseDatabase.getInstance().getReference().child("Users").child("ParentUsers").child(parentId);
                    // run some code
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

//todo check parentID and return boolean

        return false ;
    }


    public void createDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.aler_child_confirmation, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        EditText tf_parent = dialogView.findViewById(R.id.tf_parentid);

        TextView _tv_check = dialogView.findViewById(R.id.tv_check_parentid);
        TextView _tv_cancel = dialogView.findViewById(R.id.tv_cancel_parentid);
        alertDialog.setCancelable(false);
        _tv_cancel.setOnClickListener(v -> finish());
        _tv_check.setOnClickListener(v -> {
            if (isParentIdValid(tf_parent.getText().toString())) {
                //todo Go to Home
                alertDialog.dismiss();
                alertDialog.cancel();
                Toast.makeText(this, "accepted ID", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Wrong parent ID", Toast.LENGTH_SHORT).show();
                tf_parent.setText("");
            }
        });



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
