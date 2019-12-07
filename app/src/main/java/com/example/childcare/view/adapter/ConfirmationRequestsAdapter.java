package com.example.childcare.view.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.childcare.R;
import com.example.childcare.core.FirebaseUtils;
import com.example.childcare.model.Request;

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
        //TODO Child Image glide
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class ConViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, FirebaseUtils.OnCompletedListener {
        TextView child_name;
        ImageView child_image;
        Button reject;
        Button accept;
        Request request;


        ConViewHolder(@NonNull View itemView) {
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
                    FirebaseUtils.rejectParentRequest(request, this);
                    break;
                case R.id.btn_Accept_id:
                    FirebaseUtils.acceptParentRequest(request, this);
                    break;
            }
        }

        @Override
        public void onCompleted(boolean isCompletedSuccessfully) {
            if (isCompletedSuccessfully) {
                requests.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            }
        }
    }
}
