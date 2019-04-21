package com.lilliemountain.edifice.user;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lilliemountain.edifice.POJO.Committee;
import com.lilliemountain.edifice.R;

import java.util.ArrayList;
import java.util.List;

public class UserCommiteeListActivityAdapter extends RecyclerView.Adapter<UserCommiteeListActivityAdapter.UserHolder> {
    public UserCommiteeListActivityAdapter(List<Committee> list) {
        this.list = list;
    }

    List<Committee> list = new ArrayList<>();

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_committee_list, viewGroup, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder CommitteeHolder, int i) {
        CommitteeHolder.name.setText(list.get(i).getName()+"");
        CommitteeHolder.phone.setText(list.get(i).getPhone());
        CommitteeHolder.role.setText(list.get(i).getRole());
        CommitteeHolder.email.setText(list.get(i).getEmail());
        CommitteeHolder.flat.setText(list.get(i).getFlat());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        TextView name,phone,role,email,flat;
        public UserHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            role = itemView.findViewById(R.id.role);
            email = itemView.findViewById(R.id.email);
            flat = itemView.findViewById(R.id.flat);
        }
    }
}
