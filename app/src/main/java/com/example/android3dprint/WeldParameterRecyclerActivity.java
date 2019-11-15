package com.example.android3dprint;

import android.content.Intent;
import android.os.Bundle;

import com.example.android3dprint.adapter.WeldParameterAdapter;
import com.example.android3dprint.robot.SeamData;
import com.example.android3dprint.robot.SocketAsyncTask;
import com.example.android3dprint.robot.SocketMessageType;
import com.example.android3dprint.robot.WeaveData;
import com.example.android3dprint.robot.WeldData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    private SeamData[] seamDataList = new SeamData[32];
    private WeldData[] weldDataList = new WeldData[32];
    private WeaveData[] weaveDataList = new WeaveData[32];

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

        for (int index = 0; index < 32; index++) {
            seamDataList[index]=new SeamData();
            weldDataList[index]=new WeldData();
            weaveDataList[index]=new WeaveData();
        }
        adapter = new WeldParameterAdapter(seamDataList, weldDataList, weaveDataList);
        recyclerView.setAdapter(adapter);

        LoadWeldParameters();
    }

    @Override
    public void refreshUI(SocketMessageType[] socketMessageTypes) {
        Log.d(TAG,"refreshUI");
    }

    private void LoadWeldParameters() {
        String HOST = "10.0.2.2";
        int PORT = 3003;
        socketAsyncTask = new SocketAsyncTask(HOST, PORT, this);
        SocketMessageType[] socketMessageTypes;
        socketMessageTypes = new SocketMessageType[32 * 3 + 1];

        int i = -1;
        for (int index = 0; index < 32; index++) {
            socketMessageTypes[++i] = SocketMessageType.GetSeamData;
            socketMessageTypes[i].setSymbolName(String.format("seam%02d", index + 1));
            socketMessageTypes[i].setSymbolValue(seamDataList[index]);

            socketMessageTypes[++i] = SocketMessageType.GetWeldData;
            socketMessageTypes[i].setSymbolName(String.format("weld%02d", index + 1));
            socketMessageTypes[i].setSymbolValue(weldDataList[index]);
            Log.d(TAG, socketMessageTypes[i].getSymbolName().toString());

            socketMessageTypes[++i] = SocketMessageType.GetWeaveData;
            socketMessageTypes[i].setSymbolName(String.format("weave%02d", index + 1));
            socketMessageTypes[i].setSymbolValue(weaveDataList[index]);
        }
        socketMessageTypes[++i] = SocketMessageType.CloseConnection;
        socketAsyncTask.execute(socketMessageTypes);
    }
}
