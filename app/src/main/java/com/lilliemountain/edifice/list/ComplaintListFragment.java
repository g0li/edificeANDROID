package com.lilliemountain.edifice.list;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.lilliemountain.edifice.POJO.Complaint;
import com.lilliemountain.edifice.R;
import com.lilliemountain.edifice.adapters.ComplaintsAdapter;
import com.lilliemountain.edifice.create.ModifyComplaintFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ComplaintListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ComplaintListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComplaintListFragment extends Fragment implements ComplaintsAdapter.ComplaintAdapterListener, ComplaintsAdapter.OnComplaintListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ComplaintListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ComplaintListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ComplaintListFragment newInstance(String param1, String param2) {
        ComplaintListFragment fragment = new ComplaintListFragment();
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
    List<Complaint> list=new ArrayList<>();
    List<String> KeyList=new ArrayList<>();
    DatabaseReference instance,complaint;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_complaint_list, container, false);
        recyclerView=view.findViewById(R.id.reccom);
        searchView=view.findViewById(R.id.searchView);
        progressBar2=view.findViewById(R.id.progressBar2);

        progressBar2.setIndeterminate(true);
        progressBar2.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        database = FirebaseDatabase.getInstance();
        instance = database.getReference(getString(R.string.instance));
        complaint = instance.child("complaints");
        getcomplaints();
        return view;
    }

    private void getcomplaints() {
        list.clear();
        KeyList.clear();
        complaint.addValueEventListener(new ValueEventListener() {
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
                    DatabaseReference keyRef=complaint.child(key);
                    keyRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Complaint complaint=dataSnapshot.getValue(Complaint.class);
                            list.add(complaint);

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("Error at line 148", databaseError.toString());
                        }
                    });
                }

                final ComplaintsAdapter complaintsAdapter=new ComplaintsAdapter(list,ComplaintListFragment.this,ComplaintListFragment.this);
                recyclerView.setAdapter(complaintsAdapter);
                progressBar2.setVisibility(View.GONE);
                SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
                searchView.setSearchableInfo(searchManager
                        .getSearchableInfo(getActivity().getComponentName()));
                searchView.setMaxWidth(Integer.MAX_VALUE);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        complaintsAdapter.getFilter().filter(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String query) {
                        complaintsAdapter.getFilter().filter(query);
                        return false;
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e( "onCancelled: ",databaseError.toString() );
                progressBar2.setVisibility(View.GONE);
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

    @Override
    public void onComplaintSelected(Complaint complaints) {

    }

    @Override
    public void onComplaintModified(Complaint complaints, int position) {
        openFragment(ModifyComplaintFragment.newInstance(complaints,KeyList.get(position)));
    }

    private void openFragment(final Fragment fragment)   {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

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
