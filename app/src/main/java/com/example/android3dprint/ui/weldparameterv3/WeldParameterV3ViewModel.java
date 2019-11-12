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


    private String firstName="michael";
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
//
//    private String lastName="Zhu";
//    public String getLastName() {
//        return lastName;
//    }
//    public void setLastName(String firstName) {
//        this.lastName = lastName;
//    }


    MutableLiveData<WeldData> liveData;

    public MutableLiveData<WeldData> getLastName() {
        if (liveData == null) {
            liveData = new MutableLiveData<>();
            liveData.setValue(new WeldData());
        }
        return liveData;
    }

    public void setLastName(WeldData name) {
        if (liveData == null) {
            liveData = new MutableLiveData<>();
            liveData.setValue(new WeldData());
        }
        liveData.setValue(name);
    }

    private MutableLiveData<Integer> indexMutableLiveData;
    private MutableLiveData<SeamData> seamDataMutableLiveData;
    private MutableLiveData<WeldData> weldDataMutableLiveData;
    private MutableLiveData<WeaveData> weaveDataMutableLiveData;
    private MutableLiveData<String> testMutableLiveData;

    LiveData username;

    public WeldParameterV3ViewModel() {
//        String result = Repository.userName;
//        userName = Transformations.map(result, result -> result.value);
    }

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

    public LiveData<String> getTest() {
        if (testMutableLiveData == null) {
            testMutableLiveData = new MutableLiveData<String>();
            testMutableLiveData.setValue("Test");
        }
        return testMutableLiveData;
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

    public void setTest(String test) {
        if (testMutableLiveData == null) {
            testMutableLiveData = new MutableLiveData<String>();
        }
        testMutableLiveData.postValue(test);
    }
}
