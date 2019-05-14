package com.lilliemountain.edifice.adapters;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;
import android.widget.TextView;

import com.lilliemountain.edifice.POJO.Committee;
import com.lilliemountain.edifice.R;

import java.util.ArrayList;
import java.util.List;

public class CommitteeAdapter extends RecyclerView.Adapter<CommitteeAdapter.UserHolder>  implements Filterable {
    List<Committee> list=new ArrayList<>();
    private List<Committee> listFiltered;
    OnCommitteeListener onCommitteeListener;
    public CommitteeAdapter(List<Committee> list,  OnCommitteeListener onCommitteeListener) {
        this.list = list;
        this.listFiltered=this.list;
        this.onCommitteeListener = onCommitteeListener;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_committee,viewGroup,false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder complaintHolder, int i) {
        complaintHolder.username.setText(listFiltered.get(i).getName());
        complaintHolder.building.setText(listFiltered.get(i).getBuilding());
        complaintHolder.flat.setText(listFiltered.get(i).getFlat());
        complaintHolder.email.setText(listFiltered.get(i).getEmail());
        complaintHolder.role.setText(listFiltered.get(i).getRole());
        complaintHolder.complaints=listFiltered.get(i);
        complaintHolder.position=i;

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
                    List<Committee> filteredList = new ArrayList<>();
                    for (Committee row : list) {

                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getBuilding().contains(charSequence) || row.getEmail().contains(charSequence)
                                || row.getFlat().contains(charSequence)|| row.getPhone().contains(charSequence)|| row.getRole().contains(charSequence)) {
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
                listFiltered = (ArrayList<Committee>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnCommitteeListener {
        void onCommitteeModified(Committee complaints, int position);
        void onCommitteeRemoved(int position);
    }
    public class UserHolder extends RecyclerView.ViewHolder {
        TextView username,building,flat,email,role;
        Committee complaints;
        int position=0;
        public UserHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.username);
            building=itemView.findViewById(R.id.building);
            flat=itemView.findViewById(R.id.flat);
            email=itemView.findViewById(R.id.email);
            role=itemView.findViewById(R.id.role);
            itemView.findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCommitteeListener.onCommitteeRemoved(position);
                }
            });
            itemView.findViewById(R.id.modify).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDetails(v);
                }
            });
        }

    public void showDetails(View view){
        final Dialog dialog=new Dialog(view.getContext());
        dialog.setContentView(R.layout.details_committee);
        TextView name,email,building,flat;
        final Spinner role;
        name=dialog.findViewById(R.id.username);
        email=dialog.findViewById(R.id.email);
        building=dialog.findViewById(R.id.building);
        flat=dialog.findViewById(R.id.email);
        role=dialog.findViewById(R.id.role);

        name.setText(complaints.getName());
        email.setText(complaints.getEmail());
        building.setText(complaints.getBuilding());
        flat.setText(complaints.getFlat());
        final List<String>tempLst=new ArrayList<>();
        tempLst.add("Member");
        tempLst.add("Secretary");
        tempLst.add("Manager");
        tempLst.add("Treasurer");
        ArrayAdapter<String> aa=new ArrayAdapter<>(view.getContext(),android.R.layout.simple_spinner_item,tempLst);
        role.setAdapter(aa);
        role.setSelection(tempLst.indexOf(complaints.getRole()));
        dialog.show();
        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complaints.setRole(tempLst.get(role.getSelectedItemPosition()));
                onCommitteeListener.onCommitteeModified(complaints,position);
                dialog.dismiss();
            }
        });
        }
    }

}
