package com.example.android3dprint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.android3dprint.robot.ArcData;
import com.example.android3dprint.robot.SocketAsyncTask;
import com.example.android3dprint.robot.SocketMessageType;
import com.example.android3dprint.robot.WeldData;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static String HOST = "10.0.2.2";
    private static int PORT = 3003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, WeldParameterActivity.class);
        WeldData weldData=new WeldData();
        EditText editText = (EditText) findViewById(R.id.editTextInput);
        weldData.parse(editText.getText().toString());
        intent.putExtra(EXTRA_MESSAGE, weldData);
        startActivity(intent);
    }

    public void checkMessage(View view){
        EditText editText = (EditText) findViewById(R.id.editTextInput);
        WeldData weldData=new WeldData();
        weldData.parse(editText.getText().toString());
        editText = (EditText) findViewById(R.id.editTextOutput);
        editText.setText(weldData.toString());
    }

    public void socketTest(View view){
        Intent intent = new Intent(this, SocketActivity.class);
        startActivity(intent);
    }

    public void AsyncTask(View v)throws IOException {
        SocketMessageType[] socketMessageTypes=new SocketMessageType[20];
        int i=-1;
        socketMessageTypes[++i]=SocketMessageType.GetOperatingMode;
        socketMessageTypes[++i]=SocketMessageType.GetRunMode;
        socketMessageTypes[++i]=SocketMessageType.GetRobotStatus;

        socketMessageTypes[++i]=SocketMessageType.GetSignalDo;
        socketMessageTypes[i].setSignalName("sdoTest1");
        socketMessageTypes[++i]=SocketMessageType.GetSignalGo;
        socketMessageTypes[i].setSignalName("sgoTest1");
        socketMessageTypes[++i]=SocketMessageType.GetSignalAo;
        socketMessageTypes[i].setSignalName("saoTest1");

        socketMessageTypes[++i]=SocketMessageType.GetSignalDi;
        socketMessageTypes[i].setSignalName("sdiTest1");
        socketMessageTypes[++i]=SocketMessageType.GetSignalGi;
        socketMessageTypes[i].setSignalName("sgiTest1");
        socketMessageTypes[++i]=SocketMessageType.GetSignalAi;
        socketMessageTypes[i].setSignalName("saiTest1");

        int j=0;
        socketMessageTypes[++i]=SocketMessageType.SetSignalDo;
        socketMessageTypes[i].setSignalName("sdoTest1");
        socketMessageTypes[i].setSignalValue(j);
        socketMessageTypes[++i]=SocketMessageType.SetSignalGo;
        socketMessageTypes[i].setSignalName("sgoTest1");
        socketMessageTypes[i].setSignalValue(j+1);
        socketMessageTypes[++i]=SocketMessageType.SetSignalAo;
        socketMessageTypes[i].setSignalName("saoTest1");
        socketMessageTypes[i].setSignalValue(j+2);

        socketMessageTypes[++i]=SocketMessageType.GetNumData;
        socketMessageTypes[i].setSymbolName("reg1");

        socketMessageTypes[++i]=SocketMessageType.SetNumData;
        socketMessageTypes[i].setSymbolName("reg2");
        socketMessageTypes[i].setSymbolValue(j+3);

        socketMessageTypes[++i]=SocketMessageType.CloseConnection;

        SocketAsyncTask socketAsyncTask=new SocketAsyncTask(HOST,PORT,this);
        socketAsyncTask.execute(socketMessageTypes);
    }

    public void RefreshUI(SocketMessageType[] socketMessageTypes){
        EditText editText;
        for(SocketMessageType socketMessageType : socketMessageTypes){
            if(socketMessageType==null){
                continue;
            }
            switch (socketMessageType){
                case  GetNumData:
                    editText = (EditText) findViewById(R.id.editTextOutput);
                    editText.setText(socketMessageType.getSymbolValue().toString());
            }
        }
    }

}
