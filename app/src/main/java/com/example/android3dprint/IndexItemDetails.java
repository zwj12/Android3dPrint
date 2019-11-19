package com.example.android3dprint;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;

/**
 * @author CNMIZHU7
 * @since 11/17/2019
 * description：
 */
public class IndexItemDetails <K> extends ItemDetailsLookup.ItemDetails<K> {
    private static final String TAG = "IndexItemDetails";
    private int position;
    private K index;

    public IndexItemDetails(int position, K index) {
        this.position = position;
        this.index = index;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Nullable
    @Override
    public K getSelectionKey() {
        return index;
    }

}
