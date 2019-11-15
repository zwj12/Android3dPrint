package com.example.android3dprint.ui.weldparameterv3;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android3dprint.R;
import com.example.android3dprint.databinding.WeldParameterV3FragmentBinding;
import com.example.android3dprint.robot.SeamData;
import com.example.android3dprint.robot.SocketAsyncTask;
import com.example.android3dprint.robot.SocketMessageData;
import com.example.android3dprint.robot.SocketMessageType;
import com.example.android3dprint.robot.WeaveData;
import com.example.android3dprint.robot.WeldData;

public class WeldParameterV3Fragment extends Fragment
        implements AdapterView.OnItemSelectedListener, SocketAsyncTask.OnSocketListener {
    private static final String TAG = "WeldParameterV3Fragment";
    private WeldParameterV3ViewModel viewModel;
    private WeldParameterV3FragmentBinding binding;
    private SocketAsyncTask socketAsyncTask;

    private EditText editText;
    private Spinner spinner;

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
        viewModel = ViewModelProviders.of(getActivity()).get(WeldParameterV3ViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getActivity());

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        spinner = (Spinner) getActivity().findViewById(R.id.spinnerIndex);
        String[] strIndex = new String[32];
        for (int i = 0; i < 32; i++) {
            strIndex[i] = String.format("Weld%02d", i + 1);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_item, strIndex);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
//                R.array.citys, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

//        final Observer<WeaveData> nameObserver = new Observer<WeaveData>() {
//            @Override
//            public void onChanged(@Nullable final WeaveData weaveData) {
//                editText=getActivity().findViewById(R.id.editTextWeaveShape);
//                editText.setText(String.valueOf( weaveData.getWeaveShape()));
//            }
//        };
//        viewModel.getWeaveData().observe(this, nameObserver);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int index = position + 1;
        viewModel.setIndex(index);

        int i;
        socketAsyncTask = new SocketAsyncTask(viewModel.HOST, viewModel.PORT, this);
        SocketMessageData[] socketMessageDatas = new SocketMessageData[4];
        i = -1;

        socketMessageDatas[++i] =new SocketMessageData(SocketMessageType.GetSeamData);
        socketMessageDatas[i].setSymbolName(String.format("seam%02d", index));
        socketMessageDatas[i].setSymbolValue(viewModel.getSeamData().getValue());

        socketMessageDatas[++i] =new SocketMessageData( SocketMessageType.GetWeldData);
        socketMessageDatas[i].setSymbolName(String.format("weld%02d", index));
        socketMessageDatas[i].setSymbolValue(viewModel.getWeldData().getValue());

        socketMessageDatas[++i] =new SocketMessageData( SocketMessageType.GetWeaveData);
        socketMessageDatas[i].setSymbolName(String.format("weave%02d", index));
        socketMessageDatas[i].setSymbolValue(viewModel.getWeaveData().getValue());

        socketMessageDatas[++i] = new SocketMessageData(SocketMessageType.CloseConnection);

        socketAsyncTask.execute(socketMessageDatas);
        Log.d(TAG, "index:" + position + "," + id);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "onNothingSelected");
    }

    @Override
    public void refreshUI(SocketMessageData[] socketMessageDatas) {
        Log.d(TAG, "refreshUI");
        if (socketAsyncTask.isIoExceptionRaised()) {
            Toast toast = Toast.makeText(getActivity(), "The connetion may be closed, please check it!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            v.setTextColor(Color.YELLOW);
            toast.show();
        } else {
            viewModel.setSeamData(viewModel.getSeamData().getValue());
            viewModel.setWeldData(viewModel.getWeldData().getValue());
            viewModel.setWeaveData(viewModel.getWeaveData().getValue());
        }
    }

    public void SaveWeldParameter() {
        SeamData seamData = viewModel.getSeamData().getValue();
        WeldData weldData=viewModel.getWeldData().getValue();
        WeaveData weaveData=viewModel.getWeaveData().getValue();

        editText =(EditText)( getActivity().findViewById(R.id.editTextPreFlowTime));
        seamData.setPreflowTime(Double.parseDouble(editText.getText().toString()));
        editText =(EditText)( getActivity().findViewById(R.id.editTextPostFlowTime));
        seamData.setPostflowTime(Double.parseDouble(editText.getText().toString()));

        editText =(EditText)( getActivity().findViewById(R.id.editTextWeldSpeed));
        weldData.setWeldSpeed(Double.parseDouble(editText.getText().toString()));
        editText =(EditText)( getActivity().findViewById(R.id.editTextMode));
        weldData.getMainArc().setMode(Integer.parseInt(editText.getText().toString()));
        editText =(EditText)( getActivity().findViewById(R.id.editTextCurrent));
        weldData.getMainArc().setCurrent(Double.parseDouble(editText.getText().toString()));
        editText =(EditText)( getActivity().findViewById(R.id.editTextVoltage));
        weldData.getMainArc().setVoltage(Double.parseDouble(editText.getText().toString()));

        editText =(EditText)( getActivity().findViewById(R.id.editTextWeaveShape));
        weaveData.setWeaveShape(Integer.parseInt(editText.getText().toString()));
        editText =(EditText)( getActivity().findViewById(R.id.editTextWeaveLength));
        weaveData.setWeaveLength(Double.parseDouble(editText.getText().toString()));
        editText =(EditText)( getActivity().findViewById(R.id.editTextWeaveWidth));
        weaveData.setWeaveWidth(Double.parseDouble(editText.getText().toString()));
        editText =(EditText)( getActivity().findViewById(R.id.editTextWeaveHeight));
        weaveData.setWeaveHeight(Double.parseDouble(editText.getText().toString()));
        editText =(EditText)( getActivity().findViewById(R.id.editTextDwellLeft));
        weaveData.setDwellLeft(Double.parseDouble(editText.getText().toString()));
        editText =(EditText)( getActivity().findViewById(R.id.editTextDwellCenter));
        weaveData.setDwellCenter(Double.parseDouble(editText.getText().toString()));
        editText =(EditText)( getActivity().findViewById(R.id.editTextDwellRight));
        weaveData.setDwellRight(Double.parseDouble(editText.getText().toString()));
        editText =(EditText)( getActivity().findViewById(R.id.editTextWeaveDir));
        weaveData.setWeaveDir(Double.parseDouble(editText.getText().toString()));
        editText =(EditText)( getActivity().findViewById(R.id.editTextWeaveTilt));
        weaveData.setWeaveTilt(Double.parseDouble(editText.getText().toString()));
        editText =(EditText)( getActivity().findViewById(R.id.editTextWeaveOri));
        weaveData.setWeaveOri(Double.parseDouble(editText.getText().toString()));
    }
}
