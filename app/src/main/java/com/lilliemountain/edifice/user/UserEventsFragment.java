package com.lilliemountain.edifice.user;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lilliemountain.edifice.POJO.Events;
import com.lilliemountain.edifice.POJO.Users;
import com.lilliemountain.edifice.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserEventsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserEventsFragment extends Fragment implements UserEventsAdapter.OnUserEventListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UserEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserEventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserEventsFragment newInstance(String param1, String param2) {
        UserEventsFragment fragment = new UserEventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    RecyclerView events;
    UserEventsAdapter uEA;
    FirebaseDatabase database;
    DatabaseReference instance,eventsRef,keyRef,users,userUID;
    List<Events> eventsList=new ArrayList<>();
    List<Events> eventsList2=new ArrayList<>();
    Calendar calendar;
    List<String> eventsKeyList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user_events, container, false);
        events=view.findViewById(R.id.events);
        database=FirebaseDatabase.getInstance();
        instance=database.getReference(getString(R.string.instance));
        eventsRef=instance.child("events");
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventsList.clear();
                eventsKeyList.clear();
                for (DataSnapshot child :
                        dataSnapshot.getChildren()) {
                    Events events=child.getValue(Events.class);
                    if(events.getStatus().equals("Created")){
                        eventsList.add(events);
                        eventsKeyList.add(child.getKey());
                    }
                    Log.e("onDataChange: ", String.valueOf(dataSnapshot.getChildrenCount()));
                }
                uEA=new UserEventsAdapter(eventsList,UserEventsFragment.this);
                events.setLayoutManager(new LinearLayoutManager(getContext()));
                events.setAdapter(uEA);
                Log.e("scrolToPosition",scrolltoPosition+"");
                events.smoothScrollToPosition(scrolltoPosition);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        view.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(v.getContext());
                dialog.setContentView(R.layout.details_create_event);
                final EditText title,description,comments;
                title=dialog.findViewById(R.id.title);
                description=dialog.findViewById(R.id.description);
                final TextView dateView;
                dateView=dialog.findViewById(R.id.date);
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date datex = new Date();
                dateView.setText(dateFormat.format(datex));
                dateView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calendar = Calendar.getInstance();

                        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                // TODO Auto-generated method stub
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                                dateView.setText(dateFormat.format(calendar.getTime()));                            }

                        };
                        new DatePickerDialog(getContext(), date, calendar
                                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                comments=dialog.findViewById(R.id.comments);
                users=instance.child("users");
                String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                userUID=users.child(uid);
                userUID.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Users person=dataSnapshot.getValue(Users.class);
                        evnts=new Events(person.getName(),"0","0",person.getFlat(),"-",person.getBuilding(),dateView.getText().toString(),person.getEmailid(),new ArrayList<String>(),"Created");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                dialog.findViewById(R.id.create).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if(!title.getText().toString().isEmpty())
                        evnts.setTitle(title.getText().toString());
                        else
                            title.setError("Enter event name");

                        if(!description.getText().toString().isEmpty())
                            evnts.setDescription(description.getText().toString());
                        else
                            description.setError("Enter event description");

                        evnts.setComments(comments.getText().toString());
                        if(evnts.getTitle().length()>1)
                        {
                            eventsRef.push().setValue(evnts).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(v.getContext(), evnts.getTitle()+" posted!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                });
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });
        return view;
    }
    Events evnts;

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onGoing(final boolean b, final int i) {
        if(b){
        List<String> guestTemp= new ArrayList<>();
        Events events=eventsList.get(i);
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        guestTemp.add(uid);
        guestTemp.addAll(events.getGuests());
        events.setGuests(guestTemp);
        String key=eventsKeyList.get(i);
        keyRef=eventsRef.child(key);
        keyRef.setValue(events).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(b)
                {
                    Toast.makeText(getContext(), "Going to event!", Toast.LENGTH_SHORT).show();
                    scrolltoPosition=i;
                    Log.e("b",scrolltoPosition+"");

                }
                else {
                    Toast.makeText(getContext(), "Not going to event!", Toast.LENGTH_SHORT).show();
                    scrolltoPosition=i;
                    Log.e("b",scrolltoPosition+"");

                }
            }
        });
        }
        else {

            List<String> guestTemp= new ArrayList<>();
            final Events eventxs=eventsList.get(i);
            String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
            guestTemp.addAll(eventxs.getGuests());
            guestTemp.remove(uid);
            eventxs.setGuests(guestTemp);
            String key=eventsKeyList.get(i);
            keyRef=eventsRef.child(key);
            keyRef.setValue(eventxs).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(b)
                    {
                        Toast.makeText(getContext(), "Going to event!", Toast.LENGTH_SHORT).show();
                        scrolltoPosition=i;
                        Log.e("b",scrolltoPosition+"");

                    }
                    else {
                        Toast.makeText(getContext(), "Not going to event!", Toast.LENGTH_SHORT).show();
                        scrolltoPosition=i;
                    }
                }
            });
        }
    }

    @Override
    public void onStatusChanged(final String b, final int i) {
        Log.e("onStatusChanged: ", String.valueOf(b));
        Events events=eventsList.get(i);
            events.setStatus(b);
            String key=eventsKeyList.get(i);
            keyRef=eventsRef.child(key);
            keyRef.setValue(events).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getContext(), "Status Changed!", Toast.LENGTH_SHORT).show();
                }
            });
}

    int scrolltoPosition=0;
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
