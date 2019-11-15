package com.example.android3dprint.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android3dprint.R;
import com.example.android3dprint.robot.SeamData;
import com.example.android3dprint.robot.WeaveData;
import com.example.android3dprint.robot.WeldData;

/**
 * @author
 * @date 11/15/2019
 * description：
 */

public class WeldParameterAdapter extends RecyclerView.Adapter<WeldParameterAdapter.WeldParameterViewHolder>{
    private static final String TAG = "WeldParameterAdapter";
    private SeamData[] seamDataList=new SeamData[32];
    private WeldData[] weldDataList=new WeldData[32];
    private WeaveData[] weaveDataList=new WeaveData[32];

    public static class WeldParameterViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textViewSeam;
        public TextView textViewWeld;
        public TextView textViewWeave;
        public WeldParameterViewHolder(View v) {
            super(v);
            textViewSeam = itemView.findViewById(R.id.textView_seamData);
            textViewWeld = itemView.findViewById(R.id.textView_weldData);
            textViewWeave = itemView.findViewById(R.id.textView_weaveData);
        }
    }

    public WeldParameterAdapter(SeamData[] seamDataList,WeldData[] weldDataList,WeaveData[] weaveDataList) {
        this.seamDataList = seamDataList;
        this.weldDataList = weldDataList;
        this.weaveDataList = weaveDataList;
    }

    @NonNull
    @Override
    public WeldParameterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_weld_parameter, parent, false);
        WeldParameterViewHolder vh = new WeldParameterViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull WeldParameterViewHolder holder, int position) {
        holder.textViewSeam.setText(seamDataList[position].toString());
        holder.textViewWeld.setText(weldDataList[position].toString());
        holder.textViewWeave.setText(weaveDataList[position].toString());
    }

    @Override
    public int getItemCount() {
        return weldDataList.length;
    }


}
