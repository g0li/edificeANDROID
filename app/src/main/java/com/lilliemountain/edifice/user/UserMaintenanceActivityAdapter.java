package com.lilliemountain.edifice.user;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lilliemountain.edifice.POJO.maintenance.Bill;
import com.lilliemountain.edifice.R;

import java.util.ArrayList;
import java.util.List;

public class UserMaintenanceActivityAdapter extends RecyclerView.Adapter<UserMaintenanceActivityAdapter.UserHolder> {
    public UserMaintenanceActivityAdapter(List<Bill> list) {
        this.list = list;
    }

    List<Bill> list = new ArrayList<>();

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_maintenance_activity, viewGroup, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder complaintHolder, int i) {
        complaintHolder.amount.setText("₹ "+list.get(i).getAmount());
        complaintHolder.particular.setText(list.get(i).getParticular());
        complaintHolder.srno.setText("#"+(i+1));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        TextView srno,particular,amount;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            srno = itemView.findViewById(R.id.srno);
            particular = itemView.findViewById(R.id.resident);
            amount = itemView.findViewById(R.id.description);
        }

    }
}
