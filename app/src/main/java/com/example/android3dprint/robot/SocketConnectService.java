package com.example.android3dprint.robot;

import android.os.Message;
import android.util.Log;

import com.example.android3dprint.SocketActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketConnectService implements Runnable {
    private static final String TAG = "SocketConnectService";
    private String HOST = "10.0.2.2";
    private int PORT = 3003;
    private SocketHandler socketHandler;

    private PrintWriter printWriter;
    private BufferedReader in;
    private String receiveMsg;


    public SocketConnectService(String HOST, int PORT, SocketHandler socketHandler ) {
        this.HOST = HOST;
        this.PORT=PORT;
        this.socketHandler=socketHandler;
        Log.e(TAG, "SocketConnectService created");
    }

    @Override
    public void run() {//可以考虑在此处添加一个while循环，结合下面的catch语句，实现Socket对象获取失败后的超时重连，直到成功建立Socket连接
        try {
            Socket socket = new Socket(HOST, PORT);      //步骤一
            socket.setSoTimeout(60000);
            printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(   //步骤二
                    socket.getOutputStream(), "UTF-8")), true);
            SocketActivity.printWriter=printWriter;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            receiveMsg();
        } catch (Exception e) {
            Log.e(TAG, ("SocketConnectService:" + e.getMessage()));   //如果Socket对象获取失败，即连接建立失败，会走到这段逻辑
        }
    }

    private void receiveMsg() {
        try {
            while (true) {                                      //步骤三
                receiveMsg = in.readLine();
                Log.d(TAG, "readLine:" );
                if (receiveMsg != null) {
                    Log.d(TAG, "receiveMsg:" + receiveMsg);
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.obj=receiveMsg;
                    socketHandler.sendMessage(msg);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
////                            mTextView.setText(receiveMsg + "\n\n" + mTextView.getText());
//                        }
//                    });
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "receiveMsg: ");
            e.printStackTrace();
        }
    }
}
