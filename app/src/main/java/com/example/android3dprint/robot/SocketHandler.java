package com.example.android3dprint.robot;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketHandler extends Handler {
    private static final String TAG = "SocketHandler";
    public Socket socket = new Socket();
    public OutputStream outputStream;
    public InputStream inputStream;
    public PrintWriter printWriter;
    public BufferedReader bufferedReader;

    public boolean backgroundReceiveMsg=false;
    public int connectTimeOut=10000;
    public int soTimeOut=0;

    public boolean isRawData=true;

    public byte[] sendBytes = new byte[1024];
    public int sendBytesLength = 0;
    public String sendMsg;

    public byte[] receiveBytes = new byte[1024];
    public int receiveBytesLength = 0;
    public String receiveMsg;

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                Log.e(TAG, msg.obj.toString());
                break;
        }
    }
}
