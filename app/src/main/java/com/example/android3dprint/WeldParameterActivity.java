package com.example.android3dprint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.android3dprint.robot.WeldData;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class WeldParameterActivity extends AppCompatActivity {
    private static final int PORT = 9999;
    private PrintWriter printWriter;
    private BufferedReader in;
    private ExecutorService mExecutorService = null;
    private String receiveMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weld_parameter);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        WeldData weldData = (WeldData) getIntent().getSerializableExtra(MainActivity.EXTRA_MESSAGE);

//        DecimalFormat df = new DecimalFormat("0.0");
        DecimalFormat df = new DecimalFormat();

        EditText editText = (EditText) findViewById(R.id.editTextIndex);
        editText.setText(Integer.toString(weldData.getIndex()));

        editText = (EditText) findViewById(R.id.editTextWeldSpeed);
        editText.setText(df.format(weldData.getWeldSpeed()));

        editText = (EditText) findViewById(R.id.editTextMode);
        editText.setText(Integer.toString(weldData.getMainArc().getMode()));

        editText = (EditText) findViewById(R.id.editTextCurrent);
        editText.setText(new DecimalFormat("0").format(weldData.getMainArc().getCurrent()));

        editText = (EditText) findViewById(R.id.editTextVoltage);
        editText.setText(df.format(weldData.getMainArc().getVoltage()));

        mExecutorService = Executors.newCachedThreadPool();
    }

    public void refreshWeldParameter(View view) throws Exception {
//        Log.d("Michael", "RefreshWeldParameter Start");
//        int robotStatus = socketMessaging.getRobotStatus();
//        Log.d("Michael", Integer.toString((robotStatus)));
        String sendMsg ="Hello Michael";
//        mExecutorService.execute(new sendService(sendMsg));
    }

    public void connect(View view) {
//        try {
////            String strIPAddress = "172.0.0.1";
////            int intPort = 3003;
////            socketMessaging.connect(strIPAddress, intPort);
////        } catch (Exception e) {
////            // TODO Auto-generated catch block
////            Log.d("Michael",e.getMessage());
////            e.printStackTrace();
////        }

//        new Thread() {
//            public void run() {
//                try {
//                    String strIPAddress = "172.0.0.1";
//                    int intPort = 3003;
//                    socketMessaging.connect(strIPAddress, intPort);
//                    currentThread().sleep(5000);
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }.start();

//        mExecutorService.execute(new SocketConnectService());
    }

    public void close(View view) throws Exception {
//        socketMessaging.close();

//        mExecutorService.execute(new sendService("0"));
    }
}
