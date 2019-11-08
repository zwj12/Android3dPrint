package com.example.android3dprint;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android3dprint.robot.SeamData;
import com.example.android3dprint.robot.SocketMessageType;
import com.example.android3dprint.robot.WeaveData;
import com.example.android3dprint.robot.WeldData;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeldParameterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeldParameterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeldParameterFragment extends Fragment {

    private static final String TAG = "WeldParameterFragment";

    private static final String ARG_SEAM_DATA="seamdata";
    private static final String ARG_WELD_DATA="welddata";
    private static final String ARG_WEAVE_DATA="weavedata";
    private List<String> listIndex = new ArrayList<String>();
    private ArrayAdapter<String> adapterIndex;

    // TODO: Rename and change types of parameters
    private SeamData seamData;
    private WeldData weldData;
    private WeaveData weaveData;

    private EditText editText;

    private OnFragmentInteractionListener mListener;

    public WeldParameterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param seamData Parameter 1.
     * @param weldData Parameter 2.
     * @param weaveData Parameter 3.
     * @return A new instance of fragment WeldParameterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeldParameterFragment newInstance(SeamData seamData, WeldData weldData,  WeaveData weaveData) {
        WeldParameterFragment fragment = new WeldParameterFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_WELD_DATA, weldData);
        args.putSerializable(ARG_SEAM_DATA, seamData);
        args.putSerializable(ARG_WEAVE_DATA, weaveData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            seamData =(SeamData) getArguments().getSerializable(ARG_SEAM_DATA);
            weldData =(WeldData) getArguments().getSerializable(ARG_WELD_DATA);
            weaveData =(WeaveData) getArguments().getSerializable(ARG_WEAVE_DATA);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weld_parameter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        ((EditText)view.findViewById(R.id.editTextIndex)).setText(String.valueOf(weldData.getIndex()));
        ((EditText)view.findViewById(R.id.editTextWeldSpeed)).setText(String.valueOf(weldData.getWeldSpeed()));
        ((EditText)view.findViewById(R.id.editTextMode)).setText(String.valueOf(weldData.getMainArc().getMode()));

        for(int i=1;i<=32;i++){
            listIndex.add(String.format("Weld%02d", i));
        }

        Spinner spinnertext;
        spinnertext = (Spinner) view.findViewById(R.id.spinnerIndex);
        adapterIndex = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listIndex);
        adapterIndex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnertext.setAdapter(adapterIndex);
    }

    public void refreshUI(SocketMessageType[] socketMessageTypes){
        Log.d(TAG,"refreshUI");
//        ((EditText) getActivity().findViewById(R.id.editTextIndex)).setText(String.valueOf(weldData.getIndex()));
        ((EditText) getActivity().findViewById(R.id.editTextWeldSpeed)).setText(String.valueOf(weldData.getWeldSpeed()));
        ((EditText) getActivity().findViewById(R.id.editTextMode)).setText(String.valueOf(weldData.getMainArc().getMode()));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void save(){
//        editText = (EditText) getActivity().findViewById(R.id.editTextIndex);
//        weldData.setIndex(Integer.parseInt(editText.getText().toString()));

        editText = (EditText) getActivity().findViewById(R.id.editTextWeldSpeed);
        weldData.setWeldSpeed(Double.parseDouble(editText.getText().toString()));

        editText = (EditText) getActivity().findViewById(R.id.editTextMode);
        weldData.getMainArc().setMode(Integer.parseInt(editText.getText().toString()));
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
