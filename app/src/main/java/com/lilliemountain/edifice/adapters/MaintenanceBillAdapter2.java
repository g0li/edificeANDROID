package com.lilliemountain.edifice.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lilliemountain.edifice.POJO.maintenance.Particulars;
import com.lilliemountain.edifice.R;

import java.util.ArrayList;
import java.util.List;

public class MaintenanceBillAdapter2 extends RecyclerView.Adapter<MaintenanceBillAdapter2.MaintenanceParticularsHolder> {
    List<Particulars> list = new ArrayList<>();

    public MaintenanceBillAdapter2(List<Particulars> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MaintenanceParticularsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bill, viewGroup, false);
        return new MaintenanceParticularsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaintenanceParticularsHolder maintenanceHolder, int i) {
        maintenanceHolder.particular.setText(list.get(i).getName());
        maintenanceHolder.amount.setText(list.get(i).getBaseprice()+"");
        maintenanceHolder.srno.setText(i+1+"");
        if(i!=0)
        {
            maintenanceHolder.pa.setVisibility(View.GONE);
            maintenanceHolder.pam.setVisibility(View.GONE);
            maintenanceHolder.hash.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MaintenanceParticularsHolder extends RecyclerView.ViewHolder {
        TextView amount, particular,srno;
        TextView pam, pa,hash;
        public MaintenanceParticularsHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.pamount);
            particular = itemView.findViewById(R.id.resident);
            srno = itemView.findViewById(R.id.srno);
            pam = itemView.findViewById(R.id.pam);
            pa = itemView.findViewById(R.id.pa);
            hash = itemView.findViewById(R.id.hash);
        }
    }
}
