package com.lilliemountain.edifice.list;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lilliemountain.edifice.POJO.Committee;
import com.lilliemountain.edifice.POJO.Users;
import com.lilliemountain.edifice.R;
import com.lilliemountain.edifice.adapters.CommitteeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommitteeListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommitteeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommitteeListFragment extends Fragment implements CommitteeAdapter.OnCommitteeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CommitteeListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommitteeListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommitteeListFragment newInstance(String param1, String param2) {
        CommitteeListFragment fragment = new CommitteeListFragment();
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
    List<Committee> list=new ArrayList<>();
    List<String> KeyList=new ArrayList<>();
    DatabaseReference instance,committee;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_committee_list, container, false);

        recyclerView=view.findViewById(R.id.reccom);
        searchView=view.findViewById(R.id.searchView);
        progressBar2=view.findViewById(R.id.progressBar2);

        progressBar2.setIndeterminate(true);
        progressBar2.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        database = FirebaseDatabase.getInstance();
        instance = database.getReference(getString(R.string.instance));
        committee = instance.child("committee");
        getcommitteelist();
        return view;
    }

    private void getcommitteelist() {
        list.clear();
        KeyList.clear();
        committee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                KeyList.clear();
                for (DataSnapshot child :
                        dataSnapshot.getChildren()) {
                    Committee committee=child.getValue(Committee.class);
                    KeyList.add(child.getKey());
                    list.add(committee);
                }
                final CommitteeAdapter complaintsAdapter=new CommitteeAdapter(list,CommitteeListFragment.this);
                recyclerView.setAdapter(complaintsAdapter);
                progressBar2.setVisibility(View.GONE);
                SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
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
    public void onCommitteeModified(Committee complaints, int position) {
        String key=KeyList.get(position);
        DatabaseReference keyREF=committee.child(key);
        keyREF.setValue(complaints).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getContext(), "Committee updated", Toast.LENGTH_SHORT).show();
            }
        });
        Log.e("KEY::",key);
    }

    @Override
    public void onCommitteeRemoved(final int position) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        String key=KeyList.get(position);
                        DatabaseReference keyREF=committee.child(key);
                        keyREF.setValue(null);
                        Toast.makeText(getContext(), "Committee updated", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }
    Users users1;


    public void addCommittee(View view){
        final Dialog dialog=new Dialog(view.getContext());
        dialog.setContentView(R.layout.details_add_committee);
        final TextView email,building,flat;
        final Spinner role,name;
        DatabaseReference users=instance.child("users");
        name=dialog.findViewById(R.id.username);
        email=dialog.findViewById(R.id.email);
        building=dialog.findViewById(R.id.building);
        flat=dialog.findViewById(R.id.email);
        role=dialog.findViewById(R.id.role);

        final List<Users> namelisty=new ArrayList<>();
        final List<String> namelistyStr=new ArrayList<>();
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child :
                        dataSnapshot.getChildren()) {
                    Users u=child.getValue(Users.class);
                    namelisty.add(u);
                    namelistyStr.add(u.getName());
                }
                ArrayAdapter<String> namelistyStringArrayAdapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,namelistyStr);
                name.setAdapter(namelistyStringArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                users1=namelisty.get(position);
                email.setText(users1.getEmailid());
                building.setText(users1.getBuilding());
                flat.setText(users1.getFlat());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final List<String>tempLst=new ArrayList<>();
        tempLst.add("Member");
        tempLst.add("Secretary");
        tempLst.add("Manager");
        tempLst.add("Treasurer");
        ArrayAdapter<String> aa=new ArrayAdapter<>(view.getContext(),android.R.layout.simple_spinner_item,tempLst);
        role.setAdapter(aa);
        dialog.show();
        dialog.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Committee committeex=new Committee(users1.getBuilding(),users1.getEmailid(),users1.getFlat(),users1.getName(),users1.getMobile(),tempLst.get(role.getSelectedItemPosition()));
                committee.push().setValue(committeex).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(),committeex.getName()+" added as "+committeex.getRole()+" of the committee",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
            }
        });
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
