package com.example.android3dprint.robot;

import android.os.Message;
import android.util.Log;

import com.example.android3dprint.SocketActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

import javax.net.ssl.HostnameVerifier;

public class SocketConnectService implements Runnable {
    private static final String TAG = "SocketConnectService";
    private String HOST = "10.0.2.2";
    private int PORT = 3003;
    private SocketHandler socketHandler;

    public SocketConnectService(String HOST, int PORT, SocketHandler socketHandler) {
        this.HOST = HOST;
        this.PORT = PORT;
        this.socketHandler = socketHandler;
    }

    @Override
    public void run() {
        try {
//            Log.e(TAG, ("SocketConnectService:" + "Connecting"));
            SocketAddress endpoint = new InetSocketAddress(HOST, PORT);
            socketHandler.socket.connect(endpoint, socketHandler.connectTimeOut);
            socketHandler.socket.setSoTimeout(socketHandler.soTimeOut);
            socketHandler.outputStream = socketHandler.socket.getOutputStream();
            socketHandler.printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socketHandler.socket.getOutputStream(), "UTF-8")), true);

            socketHandler.inputStream = socketHandler.socket.getInputStream();
            socketHandler.bufferedReader = new BufferedReader(new InputStreamReader(
                    socketHandler.socket.getInputStream(), "UTF-8"));

            if(socketHandler.backgroundReceiveMsg){
                receiveMsg();
            }
        } catch (Exception e) {
            Log.e(TAG,  e.getMessage());
            if(e instanceof SocketTimeoutException){
                Message msg = Message.obtain();
                msg.what = -1;
                msg.obj = e;
                socketHandler.sendMessage(msg);
            }
//            e.printStackTrace();
        }
    }

    private void receiveMsg() {
        try {
            while (true) {
                if (this.socketHandler.isRawData) {
                    socketHandler.receiveBytesLength = socketHandler.inputStream.read(socketHandler.receiveBytes);
                    if (socketHandler.receiveBytesLength != -1) {
                        Message msg = Message.obtain();
                        msg.what = socketHandler.receiveBytes[0];
                        msg.arg1 = socketHandler.receiveBytes[1] + (socketHandler.receiveBytes[2] << 8);
                        msg.obj = socketHandler.receiveBytes;
                        socketHandler.sendMessage(msg);
                    } else {
//                        socketHandler.bufferedReader.close();
//                        socketHandler.inputStream.close();
                        socketHandler.socket.close();
                        Message msg = Message.obtain();
                        msg.what = -1;
                        socketHandler.sendMessage(msg);
                        break;
                    }
                } else {
                    if ((socketHandler.receiveMsg = socketHandler.bufferedReader.readLine()) != null) {
                        Message msg = Message.obtain();
                        msg.what = 255;
                        msg.obj = socketHandler.receiveMsg;
                        socketHandler.sendMessage(msg);
                    }else
                    {
//                        socketHandler.bufferedReader.close();
//                        socketHandler.inputStream.close();
                        socketHandler.socket.close();
                        Message msg = Message.obtain();
                        msg.what = -1;
                        socketHandler.sendMessage(msg);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
//            e.printStackTrace();
        }
    }
}
