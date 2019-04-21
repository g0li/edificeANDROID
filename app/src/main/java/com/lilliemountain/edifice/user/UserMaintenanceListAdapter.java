package com.lilliemountain.edifice.user;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lilliemountain.edifice.POJO.maintenance.Maintenance;
import com.lilliemountain.edifice.R;

import java.util.ArrayList;
import java.util.List;

public class UserMaintenanceListAdapter extends RecyclerView.Adapter<UserMaintenanceListAdapter.UserHolder> {
    public UserMaintenanceListAdapter(List<Maintenance> list) {
        this.list = list;
    }

    List<Maintenance> list ;

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_maintenance_bill, viewGroup, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder complaintHolder, int i) {
        complaintHolder.billfor.setText(list.get(i).getHeader().getBillfor());
        complaintHolder.statusdate.setText(list.get(i).getStatus() + " : " + list.get(i).getHeader().getDate());
        complaintHolder.maintenance = list.get(i);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView statusdate, billfor;
        Maintenance maintenance;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            statusdate = itemView.findViewById(R.id.statusdate);
            billfor = itemView.findViewById(R.id.billfor);
            billfor.setOnClickListener(this);
            statusdate.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            v.getContext().startActivity(new Intent(v.getContext(),MaintenanceActivity.class)
            .putExtra("maintenance",maintenance));
        }
    }
}
