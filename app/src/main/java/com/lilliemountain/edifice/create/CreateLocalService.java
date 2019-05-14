package com.lilliemountain.edifice.create;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lilliemountain.edifice.DashboardActivity;
import com.lilliemountain.edifice.DashboardFragment;
import com.lilliemountain.edifice.POJO.LocalServices;
import com.lilliemountain.edifice.R;
import com.lilliemountain.edifice.list.LocalServicesListFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateLocalService.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateLocalService#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateLocalService extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CreateLocalService() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateLocalService.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateLocalService newInstance(String param1, String param2) {
        CreateLocalService fragment = new CreateLocalService();
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
    LocalServices localServices;
    TextInputEditText workerName,workerNumber1,workerNumber2,workerCategory,workerOfficeAddress;
    FirebaseDatabase database;
    DatabaseReference localservices;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_create_local, container, false);
        workerName=view.findViewById(R.id.workerName);
        workerNumber1=view.findViewById(R.id.workerNumber1);
        workerNumber2=view.findViewById(R.id.workerNumber2);
        workerCategory=view.findViewById(R.id.workerCategory);
        workerOfficeAddress=view.findViewById(R.id.workerOfficeAddress);

        database=FirebaseDatabase.getInstance();
        DatabaseReference instance=database.getReference(getString(R.string.instance));
        localservices=instance.child("local-services");
        view.findViewById(R.id.create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(workerName.getText()))
                {
                    if(!TextUtils.isEmpty(workerNumber1.getText()))
                    {
                        if(!TextUtils.isEmpty(workerCategory.getText()))
                        {
                            localServices=new LocalServices(workerName.getText().toString(),workerNumber1.getText().toString(),workerNumber2.getText().toString(),workerCategory.getText().toString(),workerOfficeAddress.getText().toString());
                            localservices.push().setValue(localServices).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Snackbar.make(view,"Added "+localServices.getWorkerName(),Snackbar.LENGTH_SHORT).show();
                                    openFragment(DashboardFragment.newInstance("",""));
                                }
                            });
                        }
                        else {
                            workerCategory.setError("Enter Service Type");

                        }
                    }
                    else {
                        workerNumber1.setError("Enter Worker Contact No.");
                    }
                }
                else {
                    workerName.setError("Enter Worker Name");

                }
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void openFragment(final Fragment fragment)   {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

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
