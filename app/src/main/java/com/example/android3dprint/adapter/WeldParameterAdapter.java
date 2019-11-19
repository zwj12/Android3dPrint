package com.example.android3dprint.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android3dprint.IndexItemDetails;
import com.example.android3dprint.databinding.ViewHolderWeldParameterBinding;
import com.example.android3dprint.robot.SeamData;
import com.example.android3dprint.robot.WeaveData;
import com.example.android3dprint.robot.WeldData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael
 * @since 11/15/2019
 * description：
 */

public class WeldParameterAdapter extends RecyclerView.Adapter<WeldParameterAdapter.WeldParameterViewHolder<ViewDataBinding>> {
    private static final String TAG = "WeldParameterAdapter";
    private SeamData[] seamDataList;
    private WeldData[] weldDataList;
    private WeaveData[] weaveDataList;
    private LifecycleOwner lifecycleOwner;
    private List<Long> indexList;

    public WeldParameterAdapter(LifecycleOwner lifecycleOwner, SeamData[] seamDataList,
                                WeldData[] weldDataList, WeaveData[] weaveDataList, List<Long> indexList) {
        this.lifecycleOwner = lifecycleOwner;
        this.seamDataList = seamDataList;
        this.weldDataList = weldDataList;
        this.weaveDataList = weaveDataList;
        this.indexList = indexList;
    }

    @NonNull
    @Override
    public WeldParameterViewHolder<ViewDataBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolderWeldParameterBinding binding = ViewHolderWeldParameterBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.setLifecycleOwner(lifecycleOwner);
        return new WeldParameterViewHolder<ViewDataBinding>(binding, this.indexList);
    }

    @Override
    public void onBindViewHolder(@NonNull WeldParameterViewHolder<ViewDataBinding> holder, int position) {
        ViewHolderWeldParameterBinding binding = (ViewHolderWeldParameterBinding) (holder.binding);
        binding.setSeamData(seamDataList[position]);
        binding.setWeldData(weldDataList[position]);
        binding.setWeaveData(weaveDataList[position]);

        if (selectionTracker.isSelected(indexList.get(position))) {
            holder.binding.getRoot().setBackgroundColor(Color.parseColor("#80deea"));
        } else {
            holder.binding.getRoot().setBackgroundColor(Color.WHITE);
        }

    }

    @Override
    public int getItemCount() {
        return weldDataList.length;
    }

    private SelectionTracker selectionTracker;

    public SelectionTracker getSelectionTracker() {
        return selectionTracker;
    }

    public void setSelectionTracker(SelectionTracker selectionTracker) {
        this.selectionTracker = selectionTracker;
    }

    public static class WeldParameterViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
        private T binding;
        private List<Long> indexList;

        private WeldParameterViewHolder(T binding, List<Long> indexList) {
            super(binding.getRoot());
            this.binding = binding;
            this.indexList = indexList;
        }

        public IndexItemDetails<Long> getItemDetails() {
            return new IndexItemDetails<Long>(getAdapterPosition(), indexList.get(getAdapterPosition()));
        }

    }

}
