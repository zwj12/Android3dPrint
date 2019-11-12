package com.example.android3dprint.ui.weldparameterv3;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.android3dprint.R;
import com.example.android3dprint.databinding.WeldParameterV3FragmentBinding;
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

//        binding = DataBindingUtil.inflate(inflater, R.layout.weld_parameter_v3_fragment, container, false);
        binding = WeldParameterV3FragmentBinding.inflate(inflater, container, false);

//        viewModel = ViewModelProviders.of(this).get(WeldParameterV3ViewModel.class);
//        viewModel.getTest().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(String str) {
//                Log.d(TAG, "observe" + str);
//                EditText editText = getActivity().findViewById(R.id.editTextPostFlowTime);
//                editText.setText(str);
//            }
//        });
//        Log.d(TAG, "onCreateView: ");
//        binding.setViewModel(viewModel);
//        binding.setLifecycleOwner(getActivity());



        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(WeldParameterV3ViewModel.class);
        viewModel.getTest().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String str) {
                Log.d(TAG, "observe" + str);
                EditText editText = getActivity().findViewById(R.id.editTextPostFlowTime);
                editText.setText(str);
            }
        });
        Log.d(TAG, "onCreateView: ");
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getActivity());

//        viewModel.getWeldData().observe(this, new Observer<WeldData>() {
//            @Override
//            public void onChanged(@Nullable WeldData weldData) {
//                Log.d(TAG, "onChanged: " + weldData.toString());
//            }
//        });
//
        Button button = getActivity().findViewById(R.id.buttonUpdateTest);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.d(TAG, "checkMessage Michael");
                viewModel.setTest("Michael");
            }
        });

//        ActivityMainBinding binding = DataBindingUtil.setContentView(getActivity(), R.layout.activity_main);
//        User user = new User("Test", "User");
//        binding.setUser(user);
    }

    public void checkMessage(View view) {
        // Do something in response to button
//        Log.d(TAG,"checkMessage Michael");
//        viewModel.setTest("Michael");

    }
}
