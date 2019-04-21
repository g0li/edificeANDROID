package com.lilliemountain.edifice.create;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lilliemountain.edifice.POJO.Complaint;
import com.lilliemountain.edifice.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ModifyComplaintFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ModifyComplaintFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModifyComplaintFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Complaint mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ModifyComplaintFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ModifyComplaintFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModifyComplaintFragment newInstance(Complaint param1, String param2) {
        ModifyComplaintFragment fragment = new ModifyComplaintFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getParcelable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView title,complainttext,date,type;
    EditText admincomments;
    Spinner status;
    FirebaseDatabase database;
    List<String> stringList=new ArrayList<>();
    DatabaseReference complaint;
    ArrayAdapter<String> stringArrayAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_modify_complaint, container, false);
        title=view.findViewById(R.id.title);
        complainttext=view.findViewById(R.id.desc);
        date=view.findViewById(R.id.date);
        type=view.findViewById(R.id.type);
        admincomments=view.findViewById(R.id.admincomments);
        status=view.findViewById(R.id.status);
        database=FirebaseDatabase.getInstance();
        complaint=database.getReference("complaints");
        stringList.add("Created");
        stringList.add("Solved");
        stringList.add("Not Solved");
        stringArrayAdapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,stringList);
        status.setAdapter(stringArrayAdapter);

        title.setText(mParam1.getTitle());
        complainttext.setText(mParam1.getComplaint());
        date.setText(mParam1.getDate());
        type.setText(mParam1.getCategory());
        admincomments.setText(mParam1.getAdmincomments());
        status.setSelection(Integer.parseInt(mParam1.getStatus()));
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
