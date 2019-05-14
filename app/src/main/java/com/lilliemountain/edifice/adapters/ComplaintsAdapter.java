package com.lilliemountain.edifice.adapters;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.lilliemountain.edifice.POJO.Complaint;
import com.lilliemountain.edifice.R;

import java.util.ArrayList;
import java.util.List;

public class ComplaintsAdapter extends RecyclerView.Adapter<ComplaintsAdapter.UserHolder>  implements Filterable {
    List<Complaint> list=new ArrayList<>();
    private List<Complaint> listFiltered;
    ComplaintAdapterListener complaintsAdapterListener;
    OnComplaintListener onComplaintListener;
    public ComplaintsAdapter(List<Complaint> list, ComplaintAdapterListener complaintsAdapterListener, OnComplaintListener onComplaintListener) {
        this.list = list;
        this.listFiltered=this.list;
        this.complaintsAdapterListener = complaintsAdapterListener;
        this.onComplaintListener = onComplaintListener;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_complaint,viewGroup,false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder complaintHolder, int i) {
        complaintHolder.title.setText(listFiltered.get(i).getTitle());
        complaintHolder.desc.setText(listFiltered.get(i).getCategory());
        complaintHolder.date.setText(listFiltered.get(i).getDate());
        complaintHolder.type.setText(listFiltered.get(i).getCategory());
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
                    List<Complaint> filteredList = new ArrayList<>();
                    for (Complaint row : list) {

                        if (row.getComplaint().toLowerCase().contains(charString.toLowerCase()) || row.getCategory().contains(charSequence) || row.getTitle().contains(charSequence)
                                || row.getDate().contains(charSequence)|| row.getResidentName().contains(charSequence)) {
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
                listFiltered = (ArrayList<Complaint>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ComplaintAdapterListener {
        void onComplaintSelected(Complaint complaints);
    }
    public interface OnComplaintListener {
        void onComplaintModified(Complaint complaints, int position);
    }
    public class UserHolder extends RecyclerView.ViewHolder {
        TextView title,desc,date,type;
        Complaint complaints;
        int position=0;
        public UserHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            desc=itemView.findViewById(R.id.desc);
            date=itemView.findViewById(R.id.date);
            type=itemView.findViewById(R.id.type);
            itemView.findViewById(R.id.details).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDetails(v);
                }
            });
            itemView.findViewById(R.id.modify).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onComplaintListener.onComplaintModified(complaints,position);
                }
            });
        }

    public void showDetails(View view){
        final Dialog dialog=new Dialog(view.getContext());
        dialog.setContentView(R.layout.details_complaint);
        TextView title,desc,date,type,admincomments;
        title=dialog.findViewById(R.id.title);
        desc=dialog.findViewById(R.id.desc);
        date=dialog.findViewById(R.id.date);
        type=dialog.findViewById(R.id.type);
        admincomments=dialog.findViewById(R.id.admincomments);

        title.setText(complaints.getTitle());
        desc.setText(complaints.getComplaint());
        date.setText(complaints.getDate());
        type.setText(complaints.getCategory());
        admincomments.setText(complaints.getAdmincomments());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        }
    }

}
