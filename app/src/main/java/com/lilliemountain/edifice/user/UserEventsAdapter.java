package com.lilliemountain.edifice.user;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lilliemountain.edifice.POJO.Events;
import com.lilliemountain.edifice.POJO.Users;
import com.lilliemountain.edifice.R;

import java.util.ArrayList;
import java.util.List;

public class UserEventsAdapter extends RecyclerView.Adapter<UserEventsAdapter.UserEventsHolder> {
    public UserEventsAdapter(List<Events> list, OnUserEventListener onUserEventListener) {
        this.list = list;
        this.onUserEventListener = onUserEventListener;
    }
    OnUserEventListener onUserEventListener;
    List<Events> list = new ArrayList<>();

    @NonNull
    @Override
    public UserEventsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_events, viewGroup, false);
        return new UserEventsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserEventsHolder complaintHolder, final int i) {
        complaintHolder.resident.setText(list.get(i).getResidentName());
        complaintHolder.title.setText(list.get(i).getTitle());
        complaintHolder.description.setText(list.get(i).getDescription());
        complaintHolder.flat.setText(list.get(i).getBuilding() + " - " + list.get(i).getFlat());
        complaintHolder.date.setText(list.get(i).getDate());
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (list.get(i).getEmail().toLowerCase().equals(email.toLowerCase()))
        {
            complaintHolder.guestlist.setVisibility(View.VISIBLE);
            complaintHolder.guestlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showGuestList(v,list.get(i).getGuests());
                }
            });
        }
        else {
            complaintHolder.guestlist.setVisibility(View.GONE);
        }
        for (String string :
                list.get(i).getGuests()) {
            if(string.equals(uid)){
                complaintHolder.aSwitch.setChecked(string.equals(uid));
                break;
            }
        }
        complaintHolder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onUserEventListener.onGoing(isChecked,i);
            }
        });
        complaintHolder.closestatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(i).getStatus().equals("Created"))
                onUserEventListener.onStatusChanged("Closed",i);
                else
                onUserEventListener.onStatusChanged("Created",i);
            }
        });
        complaintHolder.goingppl.setText(list.get(i).getGuests().size()+" people going for this event.");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    interface OnUserEventListener
    {
     void onGoing(boolean b,int position);
     void onStatusChanged(String b,int position);
    }
    public class UserEventsHolder extends RecyclerView.ViewHolder {
        TextView resident,title,description,flat,date,goingppl;
        Switch aSwitch;
        Button guestlist;
        Button closestatus;
        public UserEventsHolder(@NonNull View itemView) {
            super(itemView);
            resident = itemView.findViewById(R.id.resident);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            flat = itemView.findViewById(R.id.flat);
            date = itemView.findViewById(R.id.date);
            goingppl = itemView.findViewById(R.id.goingppl);
            aSwitch=itemView.findViewById(R.id.going);
            guestlist=itemView.findViewById(R.id.guestlist);
            closestatus=itemView.findViewById(R.id.closestatus);
        }
    }
    public void showGuestList(final View view, final List<String> dataSourcxe){
        final Dialog dialog=new Dialog(view.getContext());
        final List<String> dataSource=dataSourcxe;
        dialog.setContentView(R.layout.details_guest_list);
        final RecyclerView recyclerVie=dialog.findViewById(R.id.guestlist);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference instance,users;
        instance=database.getReference(view.getContext().getString(R.string.instance));
        users=instance.child("users");
        final List<String> keyS=new ArrayList<>();
        final List<String > newDataSource=new ArrayList<>();
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot c :
                        dataSnapshot.getChildren()) {
                    keyS.add(c.getKey());
                    for (int i = 0; i < dataSource.size(); i++) {

                        if (c.getKey().equals(dataSource.get(i)))
                        {
                            Users users1 = c.getValue(Users.class);
                            newDataSource.add(users1.getName());
                        }
                    }
                }
                recyclerVie.setLayoutManager(new LinearLayoutManager(view.getContext()));
                GuestListAdapter gLA=new GuestListAdapter(newDataSource);
                recyclerVie.setAdapter(gLA);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
