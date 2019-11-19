package com.example.android3dprint;

import android.os.Bundle;

import com.example.android3dprint.adapter.WeldParameterAdapter;
import com.example.android3dprint.robot.SocketAsyncTask;
import com.example.android3dprint.robot.SocketMessageData;
import com.example.android3dprint.robot.SocketMessageType;
import com.example.android3dprint.viewmodel.WeldParameterListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class WeldParameterRecyclerActivity extends AppCompatActivity
        implements SocketAsyncTask.OnSocketListener {
    private static final String TAG = "WeldParameterRecyclerActivity";
    private RecyclerView recyclerView;
    private WeldParameterAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SocketAsyncTask socketAsyncTask;
    private WeldParameterListViewModel viewModel;

    private SelectionTracker selectionTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weld_parameter_recycler);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        viewModel = ViewModelProviders.of(this).get(WeldParameterListViewModel.class);
        adapter = new WeldParameterAdapter(this, this.viewModel.getSeamDataList().getValue(),
                this.viewModel.getWeldDataList().getValue(), this.viewModel.getWeaveDataList().getValue(),
                this.viewModel.getIndexList());

        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL));

        if (!this.viewModel.isViewModelInitialized()) {
            LoadWeldParameters();
            this.viewModel.setViewModelInitialized(true);
        }

        selectionTracker = new SelectionTracker.Builder<Long>(
                "weld-parameters-selection",
                recyclerView,
//                new StableIdKeyProvider(recyclerView),
                new IndexItemKeyProvider<>(1, this.viewModel.getIndexList()),
                new IndexItemDetailsLookup(recyclerView),
                StorageStrategy.createLongStorage())
                .withSelectionPredicate(SelectionPredicates.<Long>createSelectAnything())
                .build();

        adapter.setSelectionTracker(selectionTracker);

        selectionTracker.addObserver(new SelectionTracker.SelectionObserver() {
            @Override
            public void onItemStateChanged(@NonNull Object key, boolean selected) {
                super.onItemStateChanged(key, selected);
                Log.d(TAG,"onItemStateChanged" + key.toString() + String.valueOf(selected));
            }

            @Override
            public void onSelectionRefresh() {
                Log.d(TAG,"onSelectionRefresh");
                super.onSelectionRefresh();
            }

            @Override
            public void onSelectionChanged() {
                super.onSelectionChanged();
                if (selectionTracker.hasSelection() ) {
                    Log.d(TAG,"hasSelection");
//                    actionMode = startSupportActionMode(new ActionModeController(MainActivity.this, selectionTracker));
//                    setMenuItemTitle(selectionTracker.getSelection().size());
                } else if (!selectionTracker.hasSelection()) {
                    Log.d(TAG,"has no Selection");
//                    actionMode.finish();
//                    actionMode = null;
                } else {
                    Log.d(TAG,"else" + String.valueOf( selectionTracker.hasSelection()));
//                    setMenuItemTitle(selectionTracker.getSelection().size());
                }
//                Iterator<Item> itemIterable = selectionTracker.getSelection().iterator();
//                while (itemIterable.hasNext()) {
//                    Log.i(TAG, itemIterable.next().getItemName());
//                }
            }

            @Override
            public void onSelectionRestored() {
                super.onSelectionRestored();
                Log.d(TAG,"onSelectionRestored" );
            }
        });
    }

    @Override
    public void refreshUI(SocketMessageData[] socketMessageDatas) {
        adapter.notifyDataSetChanged();
//        viewModel.setSeamDataList(viewModel.getSeamDataList().getValue());
//        viewModel.setWeldDataList(viewModel.getWeldDataList().getValue());
//        viewModel.setWeaveDataList(viewModel.getWeaveDataList().getValue());
    }

    private void LoadWeldParameters() {
        String HOST = "10.0.2.2";
        int PORT = 3003;
        socketAsyncTask = new SocketAsyncTask(HOST, PORT, this);
        SocketMessageData[] socketMessageDatas;
        socketMessageDatas = new SocketMessageData[32 * 3 + 1];

        int i = -1;
        for (int index = 0; index < 32; index++) {
            socketMessageDatas[++i] = new SocketMessageData(SocketMessageType.GetSeamData);
            socketMessageDatas[i].setSymbolName(String.format(Locale.getDefault(),"seam%02d", index + 1));
            socketMessageDatas[i].setSymbolValue(this.viewModel.getSeamDataList().getValue()[index]);

            socketMessageDatas[++i] = new SocketMessageData(SocketMessageType.GetWeldData);
            socketMessageDatas[i].setSymbolName(String.format(Locale.getDefault(),"weld%02d", index + 1));
            socketMessageDatas[i].setSymbolValue(this.viewModel.getWeldDataList().getValue()[index]);
            Log.d(TAG, socketMessageDatas[i].getSymbolName().toString());

            socketMessageDatas[++i] = new SocketMessageData(SocketMessageType.GetWeaveData);
            socketMessageDatas[i].setSymbolName(String.format(Locale.getDefault(),"weave%02d", index + 1));
            socketMessageDatas[i].setSymbolValue(this.viewModel.getWeaveDataList().getValue()[index]);
        }

        socketMessageDatas[++i] = new SocketMessageData(SocketMessageType.CloseConnection);
        socketAsyncTask.execute(socketMessageDatas);
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        selectionTracker.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        selectionTracker.onRestoreInstanceState(savedInstanceState);

    }
}
