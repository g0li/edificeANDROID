package com.lilliemountain.edifice.list;

import android.app.SearchManager;
import android.content.Context;
import android.net.Uri;
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
import com.google.firebase.database.ValueEventListener;
import com.lilliemountain.edifice.POJO.Notices;
import com.lilliemountain.edifice.R;
import com.lilliemountain.edifice.adapters.NoticeAdapter;
import com.lilliemountain.edifice.create.CreateNoticeFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NoticeListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NoticeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoticeListFragment extends Fragment implements NoticeAdapter.OnNoticeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NoticeListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoticeListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoticeListFragment newInstance(String param1, String param2) {
        NoticeListFragment fragment = new NoticeListFragment();
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
    List<Notices> list=new ArrayList<>();
    List<String> KeyList=new ArrayList<>();
    DatabaseReference instance,notices;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notice_list, container, false);
        recyclerView=view.findViewById(R.id.recNot);
        searchView=view.findViewById(R.id.searchView);
        progressBar2=view.findViewById(R.id.progressBar2);

        progressBar2.setIndeterminate(true);
        progressBar2.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        database = FirebaseDatabase.getInstance();
        instance = database.getReference(getString(R.string.instance));
        notices = instance.child("notice-board");
        getnoticelist();
        return view;
    }

    private void getnoticelist() {

        list.clear();
        KeyList.clear();
        notices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                KeyList.clear();
                for (DataSnapshot child :
                        dataSnapshot.getChildren()) {
                    Notices n=child.getValue(Notices.class);
                    list.add(n);
                    KeyList.add(child.getKey());
                }
                final NoticeAdapter noticeAdapter=new NoticeAdapter(list,NoticeListFragment.this);
                recyclerView.setAdapter(noticeAdapter);
                progressBar2.setVisibility(View.GONE);
                SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
                searchView.setSearchableInfo(searchManager
                        .getSearchableInfo(getActivity().getComponentName()));
                searchView.setMaxWidth(Integer.MAX_VALUE);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        noticeAdapter.getFilter().filter(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String query) {
                        noticeAdapter.getFilter().filter(query);
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
    public void onNoticeModified(Notices Notices,String pos) {
        openFragment(CreateNoticeFragment.newInstance("modify",Notices,pos));
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
