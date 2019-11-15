package com.example.android3dprint.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android3dprint.R;
import com.example.android3dprint.databinding.ViewHolderWeldParameterBinding;
import com.example.android3dprint.databinding.WeldParameterV3FragmentBinding;
import com.example.android3dprint.robot.SeamData;
import com.example.android3dprint.robot.WeaveData;
import com.example.android3dprint.robot.WeldData;

/**
 * @author
 * @date 11/15/2019
 * description：
 */

public class WeldParameterAdapter extends RecyclerView.Adapter<WeldParameterAdapter.WeldParameterViewHolder<ViewDataBinding>> {
    private static final String TAG = "WeldParameterAdapter";
    private SeamData[] seamDataList = new SeamData[32];
    private WeldData[] weldDataList = new WeldData[32];
    private WeaveData[] weaveDataList = new WeaveData[32];
    private LifecycleOwner lifecycleOwner;

    public WeldParameterAdapter(LifecycleOwner lifecycleOwner,SeamData[] seamDataList, WeldData[] weldDataList, WeaveData[] weaveDataList) {
        this.lifecycleOwner=lifecycleOwner;
        this.seamDataList = seamDataList;
        this.weldDataList = weldDataList;
        this.weaveDataList = weaveDataList;
    }

    @NonNull
    @Override
    public WeldParameterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.view_holder_weld_parameter, parent, false);
        //        ViewHolderWeldParameterBinding  binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_holder_weld_parameter, parent, false);
        ViewHolderWeldParameterBinding binding = ViewHolderWeldParameterBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.setLifecycleOwner(lifecycleOwner);
        WeldParameterViewHolder vh = new WeldParameterViewHolder(binding);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull WeldParameterViewHolder<ViewDataBinding> holder, int position) {
        ViewHolderWeldParameterBinding binding =(ViewHolderWeldParameterBinding) (holder.binding);
        binding.setSeamData(seamDataList[position]);
        binding.setWeldData(weldDataList[position]);
        binding.setWeaveData(weaveDataList[position]);
    }

    @Override
    public int getItemCount() {
        return weldDataList.length;
    }


    public static class WeldParameterViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
        private T binding;

        public WeldParameterViewHolder(T binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
