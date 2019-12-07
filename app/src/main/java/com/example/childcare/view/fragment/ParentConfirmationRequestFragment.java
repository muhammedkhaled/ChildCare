package com.example.childcare.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.childcare.R;
import com.example.childcare.core.FirebaseUtils;
import com.example.childcare.view.adapter.ConfirmationRequestsAdapter;


public class ParentConfirmationRequestFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProgressBar progressLoading;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_confirmation_request, container, false);
        recyclerView = view.findViewById(R.id.parent_confirmation_Fragment_Recycler_id);
        progressLoading = view.findViewById(R.id.progress_loading);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getRequestsList();
        return view;
    }

    private void getRequestsList() {
        String parentId = FirebaseUtils.getUserId();
        FirebaseUtils.getParentRequests(parentId, requests -> {
            recyclerView.setAdapter(new ConfirmationRequestsAdapter(requests));
            progressLoading.setVisibility(View.GONE);
        });
    }

}




