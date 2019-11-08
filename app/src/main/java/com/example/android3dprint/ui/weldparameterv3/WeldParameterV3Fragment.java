package com.example.android3dprint.ui.weldparameterv3;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android3dprint.R;
import com.example.android3dprint.WeldParameterV3Activity;
import com.example.android3dprint.robot.WeldData;

public class WeldParameterV3Fragment extends Fragment {
    private static final String TAG = "WeldParameterV3Fragment";
    private WeldParameterV3ViewModel mViewModel;

    public static WeldParameterV3Fragment newInstance() {
        return new WeldParameterV3Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weld_parameter_v3_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(WeldParameterV3ViewModel.class);
        mViewModel.getWeldData().observe(this, new Observer<WeldData>() {
            @Override
            public void onChanged(@Nullable WeldData weldData) {
                Log.d(TAG, "onChanged: " + weldData.toString());
                ((TextView) getActivity().findViewById(R.id.textView_weldData)).setText(String.valueOf(weldData.toString()));
            }
        });

        getActivity().findViewById(R.id.button_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeldData weldData;
                Log.d(TAG, "Get WeldData: ");

                weldData = mViewModel.getWeldData().getValue();
                if(weldData ==null){
                    weldData=new WeldData();
                }
//                weldData=new WeldData();
                Log.d(TAG, "refreshWeldData: " + weldData.toString());
                weldData.setWeldSpeed(weldData.getWeldSpeed() + 1);
                mViewModel.setWeldData(weldData);
            }
        });
    }

}
