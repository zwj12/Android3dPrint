package com.example.android3dprint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.android3dprint.robot.ArcData;
import com.example.android3dprint.robot.SeamData;
import com.example.android3dprint.robot.SocketAsyncTask;
import com.example.android3dprint.robot.SocketMessageData;
import com.example.android3dprint.robot.SocketMessageType;
import com.example.android3dprint.robot.WeaveData;
import com.example.android3dprint.robot.WeldData;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements SocketAsyncTask.OnSocketListener{
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static String HOST = "10.0.2.2";
    private static int PORT = 3003;
    private static final String TAG = "MainActivity";
    private MutableLiveData<Integer> mNumberLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNumberLiveData = new MutableLiveData<>();

        mNumberLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                Log.d(TAG, "onChanged: " + integer);
            }
        });

    }

    public void sendMessage(View view) {

    }

    public void checkMessage(View view){
        EditText editText = (EditText) findViewById(R.id.editTextInput);
        WeldData weldData=new WeldData();
        weldData.parse(editText.getText().toString());
        editText = (EditText) findViewById(R.id.editTextOutput);
        editText.setText(weldData.toString());
    }

    public void socketTest(View view){

    }

    public void AsyncTask(View v)throws IOException {
//        SocketMessageData[] socketMessageData=new SocketMessageData[30];
//        int i=-1;
//        socketMessageData[i]=new SocketMessageData(SocketMessageType.GetOperatingMode);


//        SocketMessageType[] socketMessageTypes=new SocketMessageType[30];
//
//        socketMessageTypes[++i]=SocketMessageType.GetOperatingMode;
//        socketMessageTypes[++i]=SocketMessageType.GetRunMode;
//        socketMessageTypes[++i]=SocketMessageType.GetRobotStatus;
//
//        socketMessageTypes[++i]=SocketMessageType.GetSignalDo;
//        socketMessageTypes[i].setSignalName("sdoTest1");
//        socketMessageTypes[++i]=SocketMessageType.GetSignalGo;
//        socketMessageTypes[i].setSignalName("sgoTest1");
//        socketMessageTypes[++i]=SocketMessageType.GetSignalAo;
//        socketMessageTypes[i].setSignalName("saoTest1");
//
//        socketMessageTypes[++i]=SocketMessageType.GetSignalDi;
//        socketMessageTypes[i].setSignalName("sdiTest1");
//        socketMessageTypes[++i]=SocketMessageType.GetSignalGi;
//        socketMessageTypes[i].setSignalName("sgiTest1");
//        socketMessageTypes[++i]=SocketMessageType.GetSignalAi;
//        socketMessageTypes[i].setSignalName("saiTest1");
//
//        int j=0;
//        socketMessageTypes[++i]=SocketMessageType.SetSignalDo;
//        socketMessageTypes[i].setSignalName("sdoTest1");
//        socketMessageTypes[i].setSignalValue(j);
//        socketMessageTypes[++i]=SocketMessageType.SetSignalGo;
//        socketMessageTypes[i].setSignalName("sgoTest1");
//        socketMessageTypes[i].setSignalValue(j+1);
//        socketMessageTypes[++i]=SocketMessageType.SetSignalAo;
//        socketMessageTypes[i].setSignalName("saoTest1");
//        socketMessageTypes[i].setSignalValue(j+2);
//
//        socketMessageTypes[++i]=SocketMessageType.GetNumData;
//        socketMessageTypes[i].setSymbolName("reg1");
//
//        socketMessageTypes[++i]=SocketMessageType.SetNumData;
//        socketMessageTypes[i].setSymbolName("reg2");
//        socketMessageTypes[i].setSymbolValue(j+3);
//
//        socketMessageTypes[++i]=SocketMessageType.GetWeldData;
//        socketMessageTypes[i].setSymbolName("weld01");
//
//        socketMessageTypes[++i]=SocketMessageType.SetWeldData;
//        socketMessageTypes[i].setSymbolName("weld01");
//        WeldData weldData=new WeldData();
//        weldData.parse("[11,12,[0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0]]");
//        weldData.setWeldSpeed(15);
//        socketMessageTypes[i].setSymbolValue(weldData);
//
//        socketMessageTypes[++i]=SocketMessageType.GetSeamData;
//        socketMessageTypes[i].setSymbolName("seam1");
//
//        socketMessageTypes[++i]=SocketMessageType.SetSeamData;
//        socketMessageTypes[i].setSymbolName("seam1");
//        SeamData seamData=new SeamData();
//        seamData.parse("[0,1.5,[71,0,0,0,0,0,0,0,0],0,0,0,0,0,[72,0,0,0,0,0,0,0,0],0,0,[73,0,0,0,0,0,0,0,0],0,0,[74,0,0,0,0,0,0,0,0],2.3]");
//        seamData.setPreflowTime(2.5);
//        socketMessageTypes[i].setSymbolValue(seamData);
//
//        socketMessageTypes[++i]=SocketMessageType.GetWeaveData;
//        socketMessageTypes[i].setSymbolName("weave01");
//
//        socketMessageTypes[++i]=SocketMessageType.SetWeaveData;
//        socketMessageTypes[i].setSymbolName("weave01");
//        WeaveData weaveData=new WeaveData();
//        weaveData.parse("[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]");
//        weaveData.setWeaveShape(3);
//        socketMessageTypes[i].setSymbolValue(weaveData);
//
//        socketMessageTypes[++i]=SocketMessageType.CloseConnection;
//
//        SocketAsyncTask socketAsyncTask=new SocketAsyncTask(HOST,PORT,this);
//        socketAsyncTask.execute(socketMessageTypes);
    }

    public void openScrolling(View view) {
        // Do something in response to button
//        Intent intent = new Intent(this, WeldParameterV2Activity.class);
        Intent intent = new Intent(this, WeldParameterRecyclerActivity.class);
        startActivity(intent);
    }

    public void openViewModel(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, WeldParameterV3Activity.class);
//        Intent intent = new Intent(this, Main3Activity.class);
        startActivity(intent);
    }

    @Override
    public void refreshUI(SocketMessageData[] socketMessageDatas){
//        EditText editText;
//        for(SocketMessageType socketMessageType : socketMessageTypes){
//            if(socketMessageType==null){
//                continue;
//            }
//            switch (socketMessageType){
//                case  GetNumData:
//                    editText = (EditText) findViewById(R.id.editTextOutput);
//                    editText.setText(socketMessageType.getSymbolValue().toString());
//            }
//        }
    }
}
