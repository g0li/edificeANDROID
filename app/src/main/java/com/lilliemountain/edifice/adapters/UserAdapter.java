package com.lilliemountain.edifice.adapters;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.lilliemountain.edifice.POJO.Users;
import com.lilliemountain.edifice.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder>  implements Filterable {
    List<Users> list=new ArrayList<>();
    private List<Users> listFiltered;
    UsersAdapterListener usersAdapterListener;
    OnUserListener onUserListener;
    public UserAdapter(List<Users> list, UsersAdapterListener usersAdapterListener, OnUserListener onUserListener) {
        this.list = list;
        this.listFiltered=this.list;
        this.usersAdapterListener = usersAdapterListener;
        this.onUserListener = onUserListener;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user,viewGroup,false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder userHolder, int i) {
        userHolder.name.setText(listFiltered.get(i).getName());
        userHolder.flat.setText(listFiltered.get(i).getFlat());
        userHolder.building.setText(listFiltered.get(i).getBuilding());
        userHolder.users=listFiltered.get(i);
        userHolder.position=i;

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
                    List<Users> filteredList = new ArrayList<>();
                    for (Users row : list) {

                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getBuilding().contains(charSequence) || row.getFlat().contains(charSequence)) {
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
                listFiltered = (ArrayList<Users>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface UsersAdapterListener {
        void onUserSelected(Users users);
    }
    public interface OnUserListener {
        void onUserRemoved(Users users);
        void onUserModified(int position);
    }
    public class UserHolder extends RecyclerView.ViewHolder {
        TextView name,flat,building;
        Users users;
        int position=0;
        public UserHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            flat=itemView.findViewById(R.id.email);
            building=itemView.findViewById(R.id.building);
            itemView.findViewById(R.id.details).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDetails(v);
                }
            });
            itemView.findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onUserListener.onUserRemoved(users);
                }
            });
            itemView.findViewById(R.id.modify).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onUserListener.onUserModified(position);
                }
            });
        }

    public void showDetails(View view){
        final Dialog dialog=new Dialog(view.getContext());
        dialog.setContentView(R.layout.details_user);
        EditText name,flat,building,extrafeatures,parking,parkingslotno,vehiclemodel,twfourwheeler,emailid,mobileno,landline;
        name=dialog.findViewById(R.id.name);
        flat=dialog.findViewById(R.id.flat);
        building=dialog.findViewById(R.id.building);
        extrafeatures=dialog.findViewById(R.id.extrafeatures);
        parking=dialog.findViewById(R.id.parking);
        parkingslotno=dialog.findViewById(R.id.parkingslotno);
        vehiclemodel=dialog.findViewById(R.id.vehiclemodelno);
        twfourwheeler=dialog.findViewById(R.id.twofourwheeler);
        emailid=dialog.findViewById(R.id.email);
        mobileno=dialog.findViewById(R.id.mobile);
        landline=dialog.findViewById(R.id.landline);

        ImageView close=dialog.findViewById(R.id.close);
        TextView username=dialog.findViewById(R.id.username);

        name.setText(users.getName());
        username.setText(users.getName());
        flat.setText(users.getFlat());
        building.setText(users.getBuilding());
        extrafeatures.setText(users.getExtrafeatures());
        parking.setText(users.getParking());
        parkingslotno.setText(users.getSlotno());
        vehiclemodel.setText(users.getVmodel());
        twfourwheeler.setText(users.getTwofourwheel());
        emailid.setText(users.getEmailid());
        mobileno.setText(users.getMobile());
        landline.setText(users.getLandline());
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
