package com.example.android3dprint.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android3dprint.robot.SeamData;
import com.example.android3dprint.robot.WeaveData;
import com.example.android3dprint.robot.WeldData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CNMIZHU7
 * @since 11/15/2019
 * description：
 */
public class WeldParameterListViewModel extends ViewModel {
    private static final String TAG = "WeldParameterListViewModel";

    private boolean viewModelInitialized=false;

    private MutableLiveData<SeamData[]> seamDataListMutableLiveData;
    private MutableLiveData<WeldData[]> weldDataListMutableLiveData;
    private MutableLiveData<WeaveData[]> weaveDataListMutableLiveData;
    private ArrayList<Long> indexList;

    public ArrayList<Long> getIndexList() {
        if (indexList == null) {
            indexList = new ArrayList<Long>();
            for(Long i=0L;i<32;i++){
                indexList.add(i);
            }
        }
        return indexList;
    }

    public boolean isViewModelInitialized() {
        return viewModelInitialized;
    }

    public void setViewModelInitialized(boolean viewModelInitialized) {
        this.viewModelInitialized = viewModelInitialized;
    }

    public LiveData<SeamData[]> getSeamDataList() {
        if (seamDataListMutableLiveData == null) {
            seamDataListMutableLiveData = new MutableLiveData<SeamData[]>();
            SeamData[] seamDataList=new SeamData[32];
            for(int i=0;i<seamDataList.length;i++){
                seamDataList[i]=new SeamData();
            }
            seamDataListMutableLiveData.setValue(seamDataList);
        }
        return seamDataListMutableLiveData;
    }

    public LiveData<WeldData[]> getWeldDataList() {
        if (weldDataListMutableLiveData == null) {
            weldDataListMutableLiveData = new MutableLiveData<WeldData[]>();
            WeldData[] weldDataList=new WeldData[32];
            for(int i=0;i<weldDataList.length;i++){
                weldDataList[i]=new WeldData();
            }
            weldDataListMutableLiveData.setValue(weldDataList);
        }
        return weldDataListMutableLiveData;
    }

    public LiveData<WeaveData[]> getWeaveDataList() {
        if (weaveDataListMutableLiveData == null) {
            weaveDataListMutableLiveData = new MutableLiveData<WeaveData[]>();
            WeaveData[] weaveDataList=new WeaveData[32];
            for(int i=0;i<weaveDataList.length;i++){
                weaveDataList[i]=new WeaveData();
            }
            weaveDataListMutableLiveData.setValue(weaveDataList);
        }
        return weaveDataListMutableLiveData;
    }

    public void setSeamDataList(SeamData[] seamDataList) {
        if (seamDataListMutableLiveData == null) {
            seamDataListMutableLiveData = new MutableLiveData<SeamData[]>();
        }
        seamDataListMutableLiveData.postValue(seamDataList);
    }

    public void setWeldDataList(WeldData[] weldDataList) {
        if (weldDataListMutableLiveData == null) {
            weldDataListMutableLiveData = new MutableLiveData<WeldData[]>();
        }
        weldDataListMutableLiveData.postValue(weldDataList);
    }

    public void setWeaveDataList(WeaveData[] weaveDataList) {
        if (weaveDataListMutableLiveData == null) {
            weaveDataListMutableLiveData = new MutableLiveData<WeaveData[]>();
        }
        weaveDataListMutableLiveData.postValue(weaveDataList);
    }
}
