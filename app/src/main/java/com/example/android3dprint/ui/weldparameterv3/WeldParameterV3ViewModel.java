package com.example.android3dprint.ui.weldparameterv3;

import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.android3dprint.robot.SeamData;
import com.example.android3dprint.robot.WeaveData;
import com.example.android3dprint.robot.WeldData;

public class WeldParameterV3ViewModel extends ViewModel {
    private static final String TAG = "WeldParameterV3ViewModel";

    public String HOST = "10.0.2.2";
    public int PORT = 3003;

    private MutableLiveData<Integer> indexMutableLiveData;
    private MutableLiveData<SeamData> seamDataMutableLiveData;
    private MutableLiveData<WeldData> weldDataMutableLiveData;
    private MutableLiveData<WeaveData> weaveDataMutableLiveData;
    private MutableLiveData<String> remarkMutableLiveData;

    public LiveData<Integer> getIndex() {
        if (indexMutableLiveData == null) {
            indexMutableLiveData = new MutableLiveData<Integer>();
            indexMutableLiveData.setValue(1);
        }
        return indexMutableLiveData;
    }

    public LiveData<SeamData> getSeamData() {
        if (seamDataMutableLiveData == null) {
            seamDataMutableLiveData = new MutableLiveData<SeamData>();
            seamDataMutableLiveData.setValue(new SeamData());
        }
        return seamDataMutableLiveData;
    }

    public MutableLiveData<WeldData> getWeldData() {
        if (weldDataMutableLiveData == null) {
            weldDataMutableLiveData = new MutableLiveData<WeldData>();
            weldDataMutableLiveData.setValue(new WeldData());
        }
        return weldDataMutableLiveData;
    }

    public LiveData<WeaveData> getWeaveData() {
        if (weaveDataMutableLiveData == null) {
            weaveDataMutableLiveData = new MutableLiveData<WeaveData>();
            weaveDataMutableLiveData.setValue(new WeaveData());
        }
        return weaveDataMutableLiveData;
    }

    public MutableLiveData<String> getRemark() {
        if (remarkMutableLiveData == null) {
            remarkMutableLiveData = new MutableLiveData<String>();
            remarkMutableLiveData.setValue("remark");
        }
        return remarkMutableLiveData;
    }

    public void setIndex(Integer index) {
        if (indexMutableLiveData == null) {
            indexMutableLiveData = new MutableLiveData<Integer>();
        }
        indexMutableLiveData.postValue(index);
    }

    public void setSeamData(SeamData seamData) {
        if (seamDataMutableLiveData == null) {
            seamDataMutableLiveData = new MutableLiveData<SeamData>();
        }
        seamDataMutableLiveData.postValue(seamData);
    }

    public void setWeldData(WeldData weldData) {
        if (weldDataMutableLiveData == null) {
            weldDataMutableLiveData = new MutableLiveData<WeldData>();
        }
        weldDataMutableLiveData.postValue(weldData);
    }

    public void setWeaveData(WeaveData weaveData) {
        if (weaveDataMutableLiveData == null) {
            weaveDataMutableLiveData = new MutableLiveData<WeaveData>();
        }
        weaveDataMutableLiveData.postValue(weaveData);
    }

    public void setRemark(String remark) {
        if (remarkMutableLiveData == null) {
            remarkMutableLiveData = new MutableLiveData<String>();
        }
        remarkMutableLiveData.setValue(remark);
    }
}
