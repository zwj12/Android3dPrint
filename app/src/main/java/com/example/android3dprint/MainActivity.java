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
        SocketMessageType[] socketMessageTypes=new SocketMessageType[6];
        socketMessageTypes[0]=SocketMessageType.GetOperatingMode;
        socketMessageTypes[1]=SocketMessageType.GetRunMode;
        socketMessageTypes[2]=SocketMessageType.GetRobotStatus;
        socketMessageTypes[3]=SocketMessageType.GetSignalDo;
        socketMessageTypes[3].setSignalName("sdoTest1");
        socketMessageTypes[4]=SocketMessageType.GetSignalGo;
        socketMessageTypes[4].setSignalName("sgoTest1");
        socketMessageTypes[socketMessageTypes.length-1]=SocketMessageType.CloseConnection;
        SocketAsyncTask socketAsyncTask=new SocketAsyncTask(HOST,PORT,this);
        socketAsyncTask.execute(socketMessageTypes);
    }

}
