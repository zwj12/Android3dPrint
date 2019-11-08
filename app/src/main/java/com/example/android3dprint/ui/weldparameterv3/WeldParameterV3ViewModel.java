package com.example.android3dprint.ui.weldparameterv3;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android3dprint.robot.WeldData;

public class WeldParameterV3ViewModel extends ViewModel {
    private static final String TAG = "WeldParameterV3ViewModel";

    private MutableLiveData<WeldData> weldDataMutableLiveData;
    public LiveData<WeldData> getWeldData() {
        if (weldDataMutableLiveData == null) {
            weldDataMutableLiveData = new MutableLiveData<WeldData>();
            loadWeldData();
        }
        return weldDataMutableLiveData;
    }

    public void setWeldData(WeldData weldData) {
        Log.d(TAG, "onChanged: " + weldData.toString());
        weldDataMutableLiveData.postValue(weldData);
    }

    private void loadWeldData() {
        // Do an asynchronous operation to fetch users.
    }
}
