package com.example.android3dprint.ui.weldparameterv3;

import androidx.databinding.DataBindingUtil;
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
import com.example.android3dprint.databinding.ActivityMain3Binding;
import com.example.android3dprint.databinding.WeldParameterV3FragmentBinding;
import com.example.android3dprint.robot.SeamData;
import com.example.android3dprint.robot.WeaveData;
import com.example.android3dprint.robot.WeldData;

public class WeldParameterV3Fragment extends Fragment {
    private static final String TAG = "WeldParameterV3Fragment";
    public WeldParameterV3ViewModel viewModel;
    private WeldParameterV3FragmentBinding binding;

    private EditText editText;

    public static WeldParameterV3Fragment newInstance() {
        return new WeldParameterV3Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.weld_parameter_v3_fragment, container, false);

//        mBinding = DataBindingUtil.inflate(inflater, R.layout.weld_parameter_v3_fragment, container, false);
        binding = WeldParameterV3FragmentBinding.inflate(inflater, container, false);
        viewModel = ViewModelProviders.of(this).get(WeldParameterV3ViewModel.class);

        binding.setViewModel(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel.getWeldData().observe(this, new Observer<WeldData>() {
            @Override
            public void onChanged(@Nullable WeldData weldData) {
                Log.d(TAG, "onChanged: " + weldData.toString());
            }
        });

//        ActivityMainBinding binding = DataBindingUtil.setContentView(getActivity(), R.layout.activity_main);
//        User user = new User("Test", "User");
//        binding.setUser(user);
    }

}
