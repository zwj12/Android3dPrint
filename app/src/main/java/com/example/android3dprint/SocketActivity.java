package com.example.android3dprint;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android3dprint.robot.SocketConnectService;
import com.example.android3dprint.robot.SocketHandler;
import com.example.android3dprint.robot.SocketMessageType;
import com.example.android3dprint.robot.SocketSendService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
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

        mEditText = (EditText) findViewById(R.id.editText);
        mTextView = (TextView) findViewById(R.id.textView);
        Button mButton=(Button)findViewById(R.id.buttonConnect);
        this.socketHandler=new SocketHandler();
        mExecutorService = Executors.newCachedThreadPool();

    }

    public void connect(View view) {
//        mExecutorService.execute(new connectService());  //在一个新的线程中请求 Socket 连接
        this.socketHandler=new SocketHandler();
        mExecutorService.execute(new SocketConnectService(HOST,PORT,this.socketHandler));  //在一个新的线程中请求 Socket 连接
    }

    public void send(View view) {
        this.socketHandler.socketMessageType= SocketMessageType.GetRobotStatus;
        mExecutorService.execute(new SocketSendService(this.socketHandler));

//        String sendMsg = mEditText.getText().toString();
//        Log.d(TAG,sendMsg);
//        mExecutorService.execute(new SocketSendService(sendMsg, printWriter));

//        mExecutorService.execute(new sendService(sendMsg));
    }

    public void GetOperatingMode(View view) {
        this.socketHandler.socketMessageType= SocketMessageType.GetOperatingMode;
        mExecutorService.execute(new SocketSendService(this.socketHandler));
    }

    public void disconnect(View view) {
        this.socketHandler.socketMessageType= SocketMessageType.CloseConnection;
        mExecutorService.execute(new SocketSendService(this.socketHandler));
    }

//    private class sendService implements Runnable {
//        private String msg;
//
//        sendService(String msg) {
//            this.msg = msg;
//        }
//
//        @Override
//        public void run() {
//            printWriter.println(this.msg);
//        }
//    }

//    private class connectService implements Runnable {
//        @Override
//        public void run() {//可以考虑在此处添加一个while循环，结合下面的catch语句，实现Socket对象获取失败后的超时重连，直到成功建立Socket连接
//            try {
//                Socket socket = new Socket(HOST, PORT);      //步骤一
//                socket.setSoTimeout(60000);
//                printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(   //步骤二
//                        socket.getOutputStream(), "UTF-8")), true);
//                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
//                receiveMsg();
//            } catch (Exception e) {
//                Log.e(TAG, ("SocketConnectService:" + e.getMessage()));   //如果Socket对象获取失败，即连接建立失败，会走到这段逻辑
//            }
//        }
//    }
//
//    private void receiveMsg() {
//        try {
//            while (true) {                                      //步骤三
//                if ((receiveMsg = in.readLine()) != null) {
//                    Log.d(TAG, "receiveMsg:" + receiveMsg);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mTextView.setText(receiveMsg + "\n\n" + mTextView.getText());
//                        }
//                    });
//                }
//            }
//        } catch (IOException e) {
//            Log.e(TAG, "receiveMsg: ");
//            e.printStackTrace();
//        }
//    }
}
