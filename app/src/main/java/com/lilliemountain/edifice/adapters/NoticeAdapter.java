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

import com.lilliemountain.edifice.POJO.Notices;
import com.lilliemountain.edifice.R;

import java.util.ArrayList;
import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeHolder>  implements Filterable {
    List<Notices> list=new ArrayList<>();
    private List<Notices> listFiltered;
    OnNoticeListener onNoticeListener;
    public NoticeAdapter(List<Notices> list, OnNoticeListener onNoticeListener) {
        this.list = list;
        this.listFiltered=this.list;
        this.onNoticeListener = onNoticeListener;
    }

    @NonNull
    @Override
    public NoticeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notice,viewGroup,false);
        return new NoticeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeHolder NoticeHolder, int i) {
        NoticeHolder.title.setText(listFiltered.get(i).getNoticeTitle());
        NoticeHolder.date.setText(listFiltered.get(i).getDate());
        NoticeHolder.position=i;
        try {
            NoticeHolder.description.setText(listFiltered.get(i).getNoticeText().substring(0,100)+"...");
        } catch (Exception e) {
            NoticeHolder.description.setText(listFiltered.get(i).getNoticeText()    );

        }
        NoticeHolder.notices=listFiltered.get(i);

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
                    List<Notices> filteredList = new ArrayList<>();
                    for (Notices row : list) {
                        if (row.getNoticeTitle().toLowerCase().contains(charString.toLowerCase()) || row.getNoticeTitle().contains(charSequence) || row.getDate().contains(charSequence) || row.getNoticeType().contains(charSequence)) {
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
                listFiltered = (ArrayList<Notices>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnNoticeListener {
        void onNoticeModified(Notices Notices,String position);
    }
    public class NoticeHolder extends RecyclerView.ViewHolder {
        TextView title,description,date;
        Notices notices;
        int position=0;
        public NoticeHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            description=itemView.findViewById(R.id.desc);
            date=itemView.findViewById(R.id.date);
            itemView.findViewById(R.id.details).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDetails(v);
                }
            });
            itemView.findViewById(R.id.modify).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNoticeListener.onNoticeModified(notices,String.valueOf(position));
                }
            });
        }

    public void showDetails(View view){
        final Dialog dialog=new Dialog(view.getContext());
        dialog.setContentView(R.layout.details_notice);
        TextView title,desc;
        TextView daydate,type;
        title=dialog.findViewById(R.id.name);
        desc=dialog.findViewById(R.id.desc);
        daydate=dialog.findViewById(R.id.daydate);
        type=dialog.findViewById(R.id.type);

        ImageView close=dialog.findViewById(R.id.close);

        title.setText(notices.getNoticeTitle());
        desc.setText(notices.getNoticeText());
        daydate.setText(notices.getDay()+" , "+notices.getDate());
        type.setText(notices.getNoticeType());
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
