package com.example.android3dprint;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;

import java.util.List;

/**
 * @author CNMIZHU7
 * @since 11/17/2019
 * description：
 */
public class IndexItemKeyProvider<K> extends ItemKeyProvider<K> {
    private static final String TAG = "IndexItemKeyProvider";
    private List<K> items;

    IndexItemKeyProvider(int scope, List<K> items) {
        super(scope);
        this.items=items;
    }

    @Nullable
    @Override
    public K getKey(int position) {
        return items.get(position);
    }

    @Override
    public int getPosition(@NonNull K key) {
        return items.indexOf(key);
    }

}
