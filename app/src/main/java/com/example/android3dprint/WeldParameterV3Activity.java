package com.example.android3dprint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;

import com.example.android3dprint.robot.WeldData;
import com.example.android3dprint.ui.weldparameterv3.WeldParameterV3Fragment;
import com.example.android3dprint.ui.weldparameterv3.WeldParameterV3ViewModel;

public class WeldParameterV3Activity extends AppCompatActivity {
    private static final String TAG = "WeldParameterV3Activity";

    private WeldParameterV3ViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weld_parameter_v3_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, WeldParameterV3Fragment.newInstance())
                    .commitNow();
        }

        mViewModel = ViewModelProviders.of(this).get(WeldParameterV3ViewModel.class);

    }
}
