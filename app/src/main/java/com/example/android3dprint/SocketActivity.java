package com.example.android3dprint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
//import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android3dprint.robot.SocketConnectService;
import com.example.android3dprint.robot.SocketHandler;
import com.example.android3dprint.robot.SocketMessageType;
import com.example.android3dprint.robot.SocketSendService;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketActivity extends AppCompatActivity {
    private static final String TAG = "SocketActivity";

    private EditText mEditText;
    private TextView mTextView;
    private static final String HOST = "10.0.2.2";
    private static final int PORT = 3003;
    public static PrintWriter printWriter;
    private BufferedReader in;
    private ExecutorService mExecutorService = null;
    private String receiveMsg;

    private SocketHandler socketHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);

        mEditText = (EditText) findViewById(R.id.editText_SignalDoName);
        Button mButton = (Button) findViewById(R.id.buttonConnect);
        this.socketHandler = new SocketHandler();
        mExecutorService = Executors.newCachedThreadPool();

    }

    public void connect(View view) {
//        mExecutorService.execute(new connectService());  //在一个新的线程中请求 Socket 连接
        this.socketHandler = new SocketHandler();
        mExecutorService.execute(new SocketConnectService(HOST, PORT, this.socketHandler));  //在一个新的线程中请求 Socket 连接
    }

    public void send(View view) {
        this.socketHandler.socketMessageType = SocketMessageType.GetRobotStatus;
        mExecutorService.execute(new SocketSendService(this.socketHandler));

    }

    public void GetOperatingMode(View view) {
        this.socketHandler.socketMessageType = SocketMessageType.GetOperatingMode;
        mExecutorService.execute(new SocketSendService(this.socketHandler));
    }

    public void disconnect(View view) {
        this.socketHandler.socketMessageType = SocketMessageType.CloseConnection;
        mExecutorService.execute(new SocketSendService(this.socketHandler));
    }

    public void GetSignalDo(View view) {
        this.socketHandler.socketMessageType = SocketMessageType.GetSignalDo;
        mEditText = (EditText) findViewById(R.id.editText_SignalDoName);
        this.socketHandler.signalName = mEditText.getText().toString();
        this.socketHandler.editTextSignalValue = (EditText) findViewById(R.id.editText_SignalDoValue);
        mExecutorService.execute(new SocketSendService(this.socketHandler));

    }

    public void GetSignalGo(View view) {
        this.socketHandler.socketMessageType = SocketMessageType.GetSignalGo;
        mEditText = (EditText) findViewById(R.id.editText_SignalGoName);
        this.socketHandler.signalName = mEditText.getText().toString();
        this.socketHandler.editTextSignalValue = (EditText) findViewById(R.id.editText_SignalGoValue);
        mExecutorService.execute(new SocketSendService(this.socketHandler));

    }

    public void GetSignalAo(View view) {
        this.socketHandler.socketMessageType = SocketMessageType.GetSignalAo;
        mEditText = (EditText) findViewById(R.id.editText_SignalAoName);
        this.socketHandler.signalName = mEditText.getText().toString();
        this.socketHandler.editTextSignalValue = (EditText) findViewById(R.id.editText_SignalAoValue);
        mExecutorService.execute(new SocketSendService(this.socketHandler));

    }

    public void SetSignalDo(View view) {
        this.socketHandler.socketMessageType = SocketMessageType.SetSignalDo;
        mEditText = (EditText) findViewById(R.id.editText_SignalDoName);
        this.socketHandler.signalName = mEditText.getText().toString();
        mEditText = (EditText) findViewById(R.id.editText_SignalDoValue);
        this.socketHandler.signalValue = Double.parseDouble(mEditText.getText().toString());
        mExecutorService.execute(new SocketSendService(this.socketHandler));

    }

    public void SetSignalAo(View view) {
        this.socketHandler.socketMessageType = SocketMessageType.SetSignalAo;
        mEditText = (EditText) findViewById(R.id.editText_SignalAoName);
        this.socketHandler.signalName = mEditText.getText().toString();
        mEditText = (EditText) findViewById(R.id.editText_SignalAoValue);
        this.socketHandler.signalValue = Double.parseDouble(mEditText.getText().toString());
        mExecutorService.execute(new SocketSendService(this.socketHandler));

    }

    public void SetSignalGo(View view) {
        this.socketHandler.socketMessageType = SocketMessageType.SetSignalGo;
        mEditText = (EditText) findViewById(R.id.editText_SignalGoName);
        this.socketHandler.signalName = mEditText.getText().toString();
        mEditText = (EditText) findViewById(R.id.editText_SignalGoValue);
        this.socketHandler.signalValue = Double.parseDouble(mEditText.getText().toString());
        mExecutorService.execute(new SocketSendService(this.socketHandler));

    }

    public void GetSignalDi(View view) {
        this.socketHandler.socketMessageType = SocketMessageType.GetSignalDi;
        mEditText = (EditText) findViewById(R.id.editText_SignalDiName);
        this.socketHandler.signalName = mEditText.getText().toString();
        this.socketHandler.editTextSignalValue = (EditText) findViewById(R.id.editText_SignalDiValue);
        mExecutorService.execute(new SocketSendService(this.socketHandler));

    }

    public void GetSignalGi(View view) {
        this.socketHandler.socketMessageType = SocketMessageType.GetSignalGi;
        mEditText = (EditText) findViewById(R.id.editText_SignalGiName);
        this.socketHandler.signalName = mEditText.getText().toString();
        this.socketHandler.editTextSignalValue = (EditText) findViewById(R.id.editText_SignalGiValue);
        mExecutorService.execute(new SocketSendService(this.socketHandler));

    }

    public void GetSignalAi(View view) {
        this.socketHandler.socketMessageType = SocketMessageType.GetSignalAi;
        mEditText = (EditText) findViewById(R.id.editText_SignalAiName);
        this.socketHandler.signalName = mEditText.getText().toString();
        this.socketHandler.editTextSignalValue = (EditText) findViewById(R.id.editText_SignalAiValue);
        mExecutorService.execute(new SocketSendService(this.socketHandler));

    }

}
