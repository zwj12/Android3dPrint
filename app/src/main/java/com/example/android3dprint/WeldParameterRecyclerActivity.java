package com.example.android3dprint;

import android.content.Intent;
import android.os.Bundle;

import com.example.android3dprint.adapter.WeldParameterAdapter;
import com.example.android3dprint.robot.SeamData;
import com.example.android3dprint.robot.SocketAsyncTask;
import com.example.android3dprint.robot.SocketMessageData;
import com.example.android3dprint.robot.SocketMessageType;
import com.example.android3dprint.robot.WeaveData;
import com.example.android3dprint.robot.WeldData;
import com.example.android3dprint.ui.weldparameterv3.WeldParameterV3ViewModel;
import com.example.android3dprint.viewmodel.WeldParameterListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

public class WeldParameterRecyclerActivity extends AppCompatActivity
        implements SocketAsyncTask.OnSocketListener {
    private static final String TAG = "WeldParameterRecyclerActivity";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SocketAsyncTask socketAsyncTask;
    private WeldParameterListViewModel viewModel;

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
        adapter = new WeldParameterAdapter(this.viewModel.getSeamDataList().getValue(),
                this.viewModel.getWeldDataList().getValue(), this.viewModel.getWeaveDataList().getValue());
        recyclerView.setAdapter(adapter);

        if(!this.viewModel.isViewModelInitialized()){
            LoadWeldParameters();
            this.viewModel.setViewModelInitialized(true);
        }
    }

    @Override
    public void refreshUI(SocketMessageData[] socketMessageDatas) {
        adapter.notifyDataSetChanged();
        Log.d(TAG,"refreshUI");
    }

    private void LoadWeldParameters() {
        String HOST = "10.0.2.2";
        int PORT = 3003;
        socketAsyncTask = new SocketAsyncTask(HOST, PORT, this);
        SocketMessageData[] socketMessageDatas;
        socketMessageDatas = new SocketMessageData[32 * 3 + 1];

        int i = -1;
        for (int index = 0; index < 32; index++) {
            socketMessageDatas[++i] =new SocketMessageData(SocketMessageType.GetSeamData);
            socketMessageDatas[i].setSymbolName(String.format("seam%02d", index + 1));
            socketMessageDatas[i].setSymbolValue(this.viewModel.getSeamDataList().getValue()[index]);

            socketMessageDatas[++i] =new SocketMessageData( SocketMessageType.GetWeldData);
            socketMessageDatas[i].setSymbolName(String.format("weld%02d", index + 1));
            socketMessageDatas[i].setSymbolValue(this.viewModel.getWeldDataList().getValue()[index]);
            Log.d(TAG, socketMessageDatas[i].getSymbolName().toString());

            socketMessageDatas[++i] =new SocketMessageData( SocketMessageType.GetWeaveData);
            socketMessageDatas[i].setSymbolName(String.format("weave%02d", index + 1));
            socketMessageDatas[i].setSymbolValue(this.viewModel.getWeaveDataList().getValue()[index]);
        }

        socketMessageDatas[++i] =new SocketMessageData( SocketMessageType.CloseConnection);
        socketAsyncTask.execute(socketMessageDatas);
    }
}
