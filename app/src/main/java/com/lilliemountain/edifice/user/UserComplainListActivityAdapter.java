package com.lilliemountain.edifice.user;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lilliemountain.edifice.POJO.Complaint;
import com.lilliemountain.edifice.POJO.maintenance.Bill;
import com.lilliemountain.edifice.R;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class UserComplainListActivityAdapter extends RecyclerView.Adapter<UserComplainListActivityAdapter.UserHolder> {
    public UserComplainListActivityAdapter(List<Complaint> list) {
        this.list = list;
    }

    List<Complaint> list = new ArrayList<>();

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_complaint_list, viewGroup, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder complaintHolder, int i) {
        complaintHolder.date.setText(list.get(i).getDate()+"");
        complaintHolder.residentname.setText(list.get(i).getResidentName());
        complaintHolder.title.setText(list.get(i).getTitle());
        switch (list.get(i).getStatus())
        {
            case "1":
                complaintHolder.status.setText("Created");
                break;
            case "2":
                complaintHolder.status.setText("Solved");
                break;
            case "3":
                complaintHolder.status.setText("Not Solved");
                break;
            default:
                complaintHolder.status.setText("Contact Admin");
                break;
        }
        switch (list.get(i).getCategory())
        {
            case "0":
                complaintHolder.category.setText("Leakage");
                break;
            case "1":
                complaintHolder.category.setText("Broken Infrastructure");
                break;
            case "2":
                complaintHolder.category.setText("Gallery Light");
                break;
            case "3":
                complaintHolder.category.setText("Cleanliness Problem");
                break;
            case "4":
                complaintHolder.category.setText("Vandalism");
                break;
            case "5":
                complaintHolder.category.setText("Parking Issue");
                break;
            case "6":
                complaintHolder.category.setText("Lift Problem");
                break;
            default:
                complaintHolder.category.setText("Other");
                break;
        }
        complaintHolder.complaint.setText(list.get(i).getComplaint());
        complaintHolder.admincomments.setText(list.get(i).getAdmincomments());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        TextInputEditText date,category,residentname,title,status,complaint,admincomments;
        TextInputLayout cat,adm,com,dat;
        boolean isVisible=false;
        public UserHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            category = itemView.findViewById(R.id.category);
            residentname = itemView.findViewById(R.id.residentName);
            title = itemView.findViewById(R.id.title);
            status = itemView.findViewById(R.id.status);
            admincomments = itemView.findViewById(R.id.admincomments);
            complaint = itemView.findViewById(R.id.complaint);

            cat=itemView.findViewById(R.id.cat);
            adm=itemView.findViewById(R.id.adm);
            com=itemView.findViewById(R.id.com);
            dat=itemView.findViewById(R.id.dat);

            itemView.findViewById(R.id.details).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(adm.getVisibility()==GONE)
                    {
                        adm.setVisibility(View.VISIBLE);
                        com.setVisibility(View.VISIBLE);
                        dat.setVisibility(View.VISIBLE);
                        cat.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        adm.setVisibility(GONE);
                        com.setVisibility(GONE);
                        dat.setVisibility(GONE);
                        cat.setVisibility(GONE);
                    }
                }
            });
        }
    }
}
