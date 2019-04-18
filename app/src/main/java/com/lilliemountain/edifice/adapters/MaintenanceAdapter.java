package com.lilliemountain.edifice.adapters;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.lilliemountain.edifice.POJO.maintenance.Bill;
import com.lilliemountain.edifice.POJO.maintenance.Maintenance;
import com.lilliemountain.edifice.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MaintenanceAdapter extends RecyclerView.Adapter<MaintenanceAdapter.MaintenanceHolder>  implements Filterable {
    List<Maintenance> list=new ArrayList<>();
    private List<Maintenance> listFiltered;
    public MaintenanceAdapter(List<Maintenance> list) {
        this.list = list;
        this.listFiltered=this.list;
    }

    @NonNull
    @Override
    public MaintenanceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_maintenance,viewGroup,false);
        return new MaintenanceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaintenanceHolder maintenanceHolder, int i) {
        maintenanceHolder.name.setText(listFiltered.get(i).getHeader().getResident());
        maintenanceHolder.date.setText(listFiltered.get(i).getHeader().getDate());
        maintenanceHolder.building.setText(listFiltered.get(i).getHeader().getBuilding());
        maintenanceHolder.flat.setText(listFiltered.get(i).getHeader().getFlat());
        maintenanceHolder.totalbill.setText(listFiltered.get(i).getTotalbill()+"");
        maintenanceHolder.maintenance =listFiltered.get(i);
    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listFiltered = list;
                } else {
                    List<Maintenance> filteredList = new ArrayList<>();
                    for (Maintenance row : list) {
                        if (row.getHeader().getResident().toLowerCase().contains(charString.toLowerCase()) || row.getHeader().getBillfor().contains(charSequence) ) {
                            filteredList.add(row);
                        }
                    }

                    listFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listFiltered = (ArrayList<Maintenance>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MaintenanceHolder extends RecyclerView.ViewHolder {
        TextView name,building,flat,totalbill,date;
        Maintenance maintenance;
        public MaintenanceHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            building=itemView.findViewById(R.id.building);
            flat=itemView.findViewById(R.id.flat);
            totalbill=itemView.findViewById(R.id.totalbill);
            date=itemView.findViewById(R.id.date);
            itemView.findViewById(R.id.details).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDetails(v);
                }
            });
        }

    public void showDetails(View view){
        final Dialog dialog=new Dialog(view.getContext());
        dialog.setContentView(R.layout.details_maintenance);

        TextView payable,interestfine,pending,subtotal,totalbill,billfor,building,carpetarea,date,flat,housetype,resident;
        RecyclerView recyclerView;
        List<Bill> bills=new ArrayList<>();
        bills=maintenance.getBill();
        payable=dialog.findViewById(R.id.payable);
        subtotal=dialog.findViewById(R.id.subtotal);
        totalbill=dialog.findViewById(R.id.totalbill);
        building=dialog.findViewById(R.id.building);
        billfor=dialog.findViewById(R.id.billfor);
        carpetarea=dialog.findViewById(R.id.carpetarea);
        date=dialog.findViewById(R.id.date);
        flat=dialog.findViewById(R.id.flat);
        housetype=dialog.findViewById(R.id.housetype);
        resident=dialog.findViewById(R.id.resident);
        pending=dialog.findViewById(R.id.pending);
        interestfine=dialog.findViewById(R.id.interestfine);
        recyclerView=dialog.findViewById(R.id.recitem);
        ImageView close=dialog.findViewById(R.id.close);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        MaintenanceBillAdapter maintenanceBillAdapter=new MaintenanceBillAdapter(bills);
        recyclerView.setAdapter(maintenanceBillAdapter);
        
        payable.setText(maintenance.getPayable()+"");
        subtotal.setText(maintenance.getSubtotal()+"");
        totalbill.setText(maintenance.getTotalbill()+"");
        building.setText(maintenance.getHeader().getBuilding()+"");
        billfor.setText(maintenance.getHeader().getBillfor()+"");
        carpetarea.setText(maintenance.getHeader().getCarpetarea()+"");
        date.setText(maintenance.getHeader().getDate()+"");
        flat.setText(maintenance.getHeader().getFlat()+"");
        housetype.setText(maintenance.getHeader().getHousetype()+"");
        resident.setText(maintenance.getHeader().getResident()+"");
        pending.setText(maintenance.getPending()+"");
        interestfine.setText("00");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        }
    }

}
