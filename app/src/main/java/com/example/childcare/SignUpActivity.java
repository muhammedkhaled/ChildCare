package com.example.childcare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {
    CircleImageView circleImageView;
    EditText email_field,username_field,password_field,confirm_password_field,mobile_field,
            parentId_field;
    String email,username,password,c_password,mobile, parentId;
    Uri photopath;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ProgressDialog progressDialog;
    Intent parInetnt ;
    boolean parentFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initViews();
    }

    private void initViews() {
        circleImageView = findViewById(R.id.profile_picture);
        email_field = findViewById(R.id.email_field);
        username_field = findViewById(R.id.username_field);
        password_field = findViewById(R.id.password_field);
        confirm_password_field = findViewById(R.id.confirm_password_field);
        mobile_field = findViewById(R.id.mobile_field);
        parentId_field = findViewById(R.id.ParentId_field);
        parInetnt = getIntent();
        if (parInetnt.hasExtra("parent")){
            parentId_field.setVisibility(View.GONE);
            parentFlag = true;

        }

        auth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

       circleImageView.setOnClickListener(view -> {
//                    Intent in = new Intent(Intent.ACTION_GET_CONTENT);
//                    in.setType("image/*");
//                    startActivityForResult(in,10);
           CropImage.activity()
                   .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                   .setAspectRatio(1,1)
                   .start(SignUpActivity.this);
       });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  && resultCode == RESULT_OK &&
                data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            photopath = result.getUri();

            Glide.with(this)
                    .load(photopath)
                    .centerCrop()
                    .into(circleImageView);
        }
    }

    public void already(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void register(View view) {
        email = email_field.getText().toString();
        username = username_field.getText().toString();
        password = password_field.getText().toString();
        c_password = confirm_password_field.getText().toString();
        mobile = mobile_field.getText().toString();
        parentId = parentId_field.getText().toString();

        parInetnt = getIntent();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "enter email", Toast.LENGTH_SHORT).show();
            email_field.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), "enter username",
                    Toast.LENGTH_SHORT).show();
            username_field.requestFocus();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "password is too short",
                    Toast.LENGTH_SHORT).show();
            password_field.requestFocus();
            return;
        }

        if (!c_password.equals(password)) {
            Toast.makeText(getApplicationContext(), "password is not matching",
                    Toast.LENGTH_SHORT).show();
            confirm_password_field.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(getApplicationContext(), "enter mobile number",
                    Toast.LENGTH_SHORT).show();
            mobile_field.requestFocus();
            return;
        }

        if (!parentFlag){
            if (TextUtils.isEmpty(parentId)){
                Toast.makeText(getApplicationContext(), "enter your parentId",
                        Toast.LENGTH_SHORT).show();
                parentId_field.requestFocus();
            return; }
        }

        if (photopath == null) {
            Toast.makeText(getApplicationContext(), "select photo", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        createUser(email,password,username,mobile, parentId);
    }

    private void createUser(final String email, String password, final String username,
                            final String mobile, final String parentid) {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();

                        if (user != null)
                            user.sendEmailVerification();
                        String uId = task.getResult().getUser().getUid();
                        uploadImage(email,username,mobile,parentid,uId);
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage()
                                , Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    private void uploadImage(final String email, final String username, final String mobile
            , final String parentid, final String uId) {
        UploadTask uploadTask;
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("images/" + photopath.getLastPathSegment());

        uploadTask = storageReference.putFile(photopath);

        Task<Uri> task = uploadTask.continueWithTask(task1 -> storageReference.getDownloadUrl()).
                addOnCompleteListener(task12 -> {
                    if (task12.isSuccessful()) {
                        Uri image_uri = task12.getResult();
                        String image_url = image_uri.toString();
                            addUser(email,username,mobile,parentid,uId,image_url);
                    }
                });
    }

    private void addUser(String email, String username, String mobile, String parentid, String id,
                         String photo) {
        UserModel userModel;
            if (!parentFlag){
                userModel = new UserModel(email,username,mobile,parentid,photo);
                databaseReference.child("Users").child("ChildUsers").child(id).setValue(userModel);
            }else {
                userModel = new UserModel(email,username,mobile,photo);
                databaseReference.child("Users").child("ParentUsers").child(id).setValue(userModel);
            }

        progressDialog.dismiss();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
