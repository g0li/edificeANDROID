package com.lilliemountain.edifice.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.lilliemountain.edifice.POJO.LocalServices;
import com.lilliemountain.edifice.R;

import java.util.ArrayList;
import java.util.List;

public class LocalServicesAdapter extends RecyclerView.Adapter<LocalServicesAdapter.UserHolder>  implements Filterable {
    List<LocalServices> list=new ArrayList<>();
    private List<LocalServices> listFiltered;
    public LocalServicesAdapter(List<LocalServices> list) {
        this.list = list;
        this.listFiltered=this.list;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_local_services,viewGroup,false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder localServiceHolder, int i) {
        localServiceHolder.workerName.setText(listFiltered.get(i).getWorkerName());
        localServiceHolder.workerCategory.setText(listFiltered.get(i).getWorkerCategory());
        localServiceHolder.workerOfficeAddress.setText(listFiltered.get(i).getWorkerOfficeAddress());
        localServiceHolder.workerNumber1.setText(listFiltered.get(i).getWorkerNumber1());
        localServiceHolder.workerNumber2.setText(listFiltered.get(i).getWorkerNumber2());
        localServiceHolder.localServices=listFiltered.get(i);

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
                    List<LocalServices> filteredList = new ArrayList<>();
                    for (LocalServices row : list) {

                        if (row.getWorkerName().toLowerCase().contains(charString.toLowerCase()) || row.getWorkerCategory().toLowerCase().contains(charSequence.toString().toLowerCase()) || row.getWorkerNumber1().toLowerCase().contains(charSequence.toString().toLowerCase())
                                || row.getWorkerNumber2().toLowerCase().contains(charSequence.toString().toLowerCase())|| row.getWorkerOfficeAddress().toLowerCase().contains(charSequence.toString().toLowerCase())) {
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
                listFiltered = (ArrayList<LocalServices>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        TextView workerName,workerCategory,workerOfficeAddress;
        Button workerNumber1,workerNumber2;
        LocalServices localServices;
        int position=0;
        public UserHolder(@NonNull View itemView) {
            super(itemView);
            workerName=itemView.findViewById(R.id.workerName);
            workerCategory=itemView.findViewById(R.id.workerCategory);
            workerOfficeAddress=itemView.findViewById(R.id.workerOfficeAddress);
            workerNumber1=itemView.findViewById(R.id.workerNumber1);
            workerNumber2=itemView.findViewById(R.id.workerNumber2);
        }

    }

}
