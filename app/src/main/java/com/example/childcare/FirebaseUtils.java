package com.example.childcare;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUtils {

    private static final String USER_DATABASE = "Users";
    private static final String USER_DATABASE_CHILD_USERS = "ChildUsers";
    private static final String USER_DATABASE_PARENT_USERS = "ParentUsers";

    public static DatabaseReference getChildDatabaseReference(){
        return FirebaseDatabase.getInstance().getReference().child(USER_DATABASE).child(USER_DATABASE_CHILD_USERS);
    }

    public static DatabaseReference getParentDatabaseReference(){
        return FirebaseDatabase.getInstance().getReference().child(USER_DATABASE).child(USER_DATABASE_PARENT_USERS);
    }

    public static StorageReference getImagesStorage(){
        return FirebaseStorage.getInstance().getReference().child("images");
    }

}
