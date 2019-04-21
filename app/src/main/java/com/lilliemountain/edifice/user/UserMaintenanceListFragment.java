package com.lilliemountain.edifice.user;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.lilliemountain.edifice.POJO.Users;
import com.lilliemountain.edifice.POJO.maintenance.Maintenance;
import com.lilliemountain.edifice.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserMaintenanceListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserMaintenanceListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserMaintenanceListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UserMaintenanceListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserMaintenanceListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserMaintenanceListFragment newInstance(String param1, String param2) {
        UserMaintenanceListFragment fragment = new UserMaintenanceListFragment();
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
    TextView greetings,nextbill,amount;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference instance,maintenance,user,users;
    UserMaintenanceListAdapter userMLAdater;
    List<Maintenance> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_user_maintenance_list, container, false);
        greetings=view.findViewById(R.id.greetings);
        nextbill=view.findViewById(R.id.nextbill);
        amount=view.findViewById(R.id.description);
        recyclerView=view.findViewById(R.id.bills);
        database=FirebaseDatabase.getInstance();
        instance=database.getReference(getString(R.string.instance));
        maintenance=instance.child("maintenance");
        users=instance.child("users");
        final String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        user=maintenance.child(uid);
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 :
                        dataSnapshot.getChildren()) {
                    if (dataSnapshot1.getKey().equals(uid)) {
                        Users loggedinUser=dataSnapshot1.getValue(Users.class);
                        greetings.setText("Hello\n"+loggedinUser.getBuilding()+"\n"+loggedinUser.getFlat());

                        user.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                list.clear();
                                GenericTypeIndicator<List<Maintenance>> t=new GenericTypeIndicator<List<Maintenance>>() {};
                                list.addAll(dataSnapshot.getValue(t));
                                nextbill.setText("next bill is due "+list.get(list.size()-1).getHeader().getDate());
                                amount.setText("₹"+list.get(list.size()-1).getTotalbill());
                                userMLAdater=new UserMaintenanceListAdapter(list);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(userMLAdater);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

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
