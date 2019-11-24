package com.example.childcare;


import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.childcare.data.Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class ParentConfirmationRequestFragment extends Fragment {
    RecyclerView recyclerView;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_parent_confirmation_request, container, false);
        recyclerView = view.findViewById(R.id.parent_confirmation_Fragment_Recycler_id);
         recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getRequestsList();
        return view;
    }

    private void getRequestsList() {
        String parentId = "123456789";
        ArrayList<Request> requests = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Requests").orderByChild("parentId").equalTo(parentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Request request = snapshot.getValue(Request.class);
                    request.setId(snapshot.getKey());
                    requests.add(request);
                }
                recyclerView.setAdapter(new ConfirmationRequestsAdapter(requests));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Test", "onDataChange: ");
            }
        });

    }


}




