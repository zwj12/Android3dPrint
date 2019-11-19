package com.example.android3dprint;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android3dprint.adapter.WeldParameterAdapter;

/**
 * @author CNMIZHU7
 * @since 11/17/2019
 * description：
 */
public class IndexItemDetailsLookup extends ItemDetailsLookup<Long> {
    private static final String TAG = "IndexItemDetailsLookup";

    private final RecyclerView recyclerView;

    IndexItemDetailsLookup(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null) {
            RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);
            if (holder instanceof WeldParameterAdapter.WeldParameterViewHolder) {
                return ((WeldParameterAdapter.WeldParameterViewHolder) holder).getItemDetails();
            }
        }
        return null;
    }

}
