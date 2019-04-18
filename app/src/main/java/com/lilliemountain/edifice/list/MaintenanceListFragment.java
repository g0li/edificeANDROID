package com.lilliemountain.edifice.list;

import android.app.SearchManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.lilliemountain.edifice.POJO.Notices;
import com.lilliemountain.edifice.POJO.maintenance.Maintenance;
import com.lilliemountain.edifice.R;
import com.lilliemountain.edifice.adapters.MaintenanceAdapter;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MaintenanceListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MaintenanceListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MaintenanceListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MaintenanceListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MaintenanceListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MaintenanceListFragment newInstance(String param1, String param2) {
        MaintenanceListFragment fragment = new MaintenanceListFragment();
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

    FirebaseDatabase database;
    RecyclerView recyclerView;
    SearchView searchView;
    ProgressBar progressBar2;
    List<Maintenance> list=new ArrayList<>();
    List<String> KeyList=new ArrayList<>();
    DatabaseReference instance,maintenance;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_maintenance_list, container, false);
        onButtonPressed(MaintenanceListFragment.class.getSimpleName());
        recyclerView=view.findViewById(R.id.recMai);
        searchView=view.findViewById(R.id.searchView);
        progressBar2=view.findViewById(R.id.progressBar2);

        progressBar2.setIndeterminate(true);
        progressBar2.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        database = FirebaseDatabase.getInstance();
        instance = database.getReference(getString(R.string.instance));
        maintenance = instance.child("maintenance");

        getmaintenancelist();

        return view;
    }

    private void getmaintenancelist() {

        list.clear();
        KeyList.clear();
        maintenance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                KeyList.clear();
                for (DataSnapshot child :
                        dataSnapshot.getChildren()) {
                    KeyList.add(child.getKey());
                }

                for (String key:
                        KeyList) {
                    DatabaseReference keyRef=maintenance.child(key);
                    keyRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            GenericTypeIndicator<List<Maintenance>> t = new GenericTypeIndicator<List<Maintenance>>() {};
                            list.addAll(dataSnapshot.getValue(t));

                            final MaintenanceAdapter maintenanceAdapter=new MaintenanceAdapter(list);
                            recyclerView.setAdapter(maintenanceAdapter);
                            progressBar2.setVisibility(View.GONE);
                            SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
                            searchView.setSearchableInfo(searchManager
                                    .getSearchableInfo(getActivity().getComponentName()));
                            searchView.setMaxWidth(Integer.MAX_VALUE);
                            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    maintenanceAdapter.getFilter().filter(query);
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String query) {
                                    maintenanceAdapter.getFilter().filter(query);
                                    return false;
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("Error at line 148", databaseError.toString());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error at line 156", databaseError.toString());

            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
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
        void onFragmentInteraction(String uri);
    }
}
