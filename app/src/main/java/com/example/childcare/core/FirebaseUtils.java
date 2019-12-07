package com.example.childcare.core;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.childcare.model.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FirebaseUtils {

    private static final String USER_DATABASE = "Users";
    private static final String REQUESTS_DATABASE = "Requests";
    private static final String IMAGES_STORAGE = "images";
    private static final String USER_DATABASE_CHILD_USERS = "ChildUsers";
    private static final String USER_DATABASE_PARENT_USERS = "ParentUsers";
    private static final String REQUEST_ACCEPTED = "accept";
    private static final String REQUEST_REJECTED = "reject";
    private static final String ATTRIBUTE_PARENT_ID = "parentId";
    private static final String ATTRIBUTE_ACTION = "action";


    public static void login(String email, String password, OnCompletedListener onCompleteListener) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> onCompleteListener.onCompleted(task.isSuccessful()));
    }


    public static DatabaseReference getChildDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference().child(USER_DATABASE).child(USER_DATABASE_CHILD_USERS);
    }

    public static DatabaseReference getParentDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference().child(USER_DATABASE).child(USER_DATABASE_PARENT_USERS);
    }

    public static StorageReference getImagesStorage() {
        return FirebaseStorage.getInstance().getReference().child(IMAGES_STORAGE);
    }

    private static Query getRequestsQuery(String parentId) {
        return FirebaseDatabase.getInstance().getReference(REQUESTS_DATABASE)
                .orderByChild(ATTRIBUTE_PARENT_ID).equalTo(parentId);
    }

    public static void acceptParentRequest(Request request, OnCompletedListener onCompletedListener) {
        updateRequestAction(REQUEST_ACCEPTED, request, onCompletedListener);
    }

    public static void rejectParentRequest(Request request, OnCompletedListener onCompletedListener) {
        updateRequestAction(REQUEST_REJECTED, request, onCompletedListener);
        setChildParentIdWithNull(request);

    }

    private static void setChildParentIdWithNull(Request request) {
        getChildDatabaseReference().child(request.getChildId()).child(ATTRIBUTE_PARENT_ID).setValue("");
    }

    private static void updateRequestAction(String action, Request request, OnCompletedListener onCompletedListener) {
        FirebaseDatabase.getInstance().getReference(REQUESTS_DATABASE).child(request.getId()).child(ATTRIBUTE_ACTION)
                .setValue(action).addOnCompleteListener(task -> onCompletedListener.onCompleted(task.isSuccessful()))
                .addOnFailureListener(e -> onCompletedListener.onCompleted(false));
    }

    public static void getParentRequests(String parentId, OnRequestCompletedListener onRequestCompletedListener) {
        FirebaseUtils.getRequestsQuery(parentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Request> requests = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Request request = snapshot.getValue(Request.class);
                    if (request != null && (request.getAction() == null || request.getAction().isEmpty())) {
                        request.setId(snapshot.getKey());
                        requests.add(request);
                    }
                }
                onRequestCompletedListener.onCompleted(requests);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Test", "onDataChange: ");
                onRequestCompletedListener.onCompleted(new ArrayList<>());
            }
        });
    }

    public static String getUserId() {
        return FirebaseAuth.getInstance().getUid();
    }


    public interface OnCompletedListener {
        void onCompleted(boolean isCompletedSuccessfully);
    }

    public interface OnRequestCompletedListener {
        void onCompleted(ArrayList<Request> requests);
    }

}
