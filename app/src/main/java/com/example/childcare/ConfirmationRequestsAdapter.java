package com.example.childcare;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.childcare.data.Request;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ConfirmationRequestsAdapter extends RecyclerView.Adapter<ConfirmationRequestsAdapter.ConViewHolder> {

    private ArrayList<Request> requests;

    public ConfirmationRequestsAdapter(ArrayList<Request> requests) {
        this.requests = requests;
    }

    @NonNull
    @Override
    public ConViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.parent_request_element, parent, false));


    }

    @Override
    public void onBindViewHolder(@NonNull ConViewHolder holder, int position) {
        holder.request = requests.get(position);
        holder.child_name.setText(requests.get(position).getChildName());

    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class ConViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView child_name;
        ImageView child_image;
        Button reject,accept ;
        Request request;


        public ConViewHolder(@NonNull View itemView) {
            super(itemView);
            child_name = itemView.findViewById(R.id.tv_Child_name_id);
            child_image = itemView.findViewById(R.id.iv_child_image_id);
            reject = itemView.findViewById(R.id.btn_reject_id);
            accept = itemView.findViewById(R.id.btn_Accept_id);
            reject.setOnClickListener(this);
            accept.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_reject_id:
                    updateRequestAction("Reject");
                    setParentIdToNull(request.getChildId());
                    break;
                case R.id.btn_Accept_id:
                    updateRequestAction("Accept");
                    break;
            }
        }

        private void setParentIdToNull(String childId) {
            FirebaseDatabase.getInstance().getReference("Child").child(childId).child("parentId").setValue("");
        }

        private void updateRequestAction(String action) {
            FirebaseDatabase.getInstance().getReference("Requests").child(request.getId()).child("action").setValue(action);
        }
    }
}
