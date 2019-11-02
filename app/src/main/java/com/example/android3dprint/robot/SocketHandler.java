package com.example.android3dprint.robot;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
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
    public DataInputStream dataInputStream;
    public DataOutputStream dataOutputStream;

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

    public SocketMessageType socketMessageType;
    public String signalName;
    public double signalValue;
    public EditText editTextSignalValue;

    @Override
    public void handleMessage(Message msg) {
        Log.d(TAG, String.format("what=%d, arg1=%d",msg.what,msg.arg1));
        if(msg.what==SocketMessageType.CloseConnection.getRequestCommand()){
            try
            {
                this.socket.close();
                Log.d(TAG, "Socket is closed");
            }
            catch (IOException e)
            {
                Log.d(TAG,"Socket close is not OK");
            }
        }else if(msg.what==SocketMessageType.GetSignalDo.getRequestCommand())
        {
            Log.d(TAG,msg.obj.toString() + ":" + msg.arg1);
            editTextSignalValue.setText(Integer.toString( msg.arg1));
        }else if(msg.what==SocketMessageType.GetSignalGo.getRequestCommand())
        {
            Log.d(TAG,msg.obj.toString() + ":" + msg.arg1);
            editTextSignalValue.setText(Integer.toString( msg.arg1));
        }else if(msg.what==SocketMessageType.GetSignalAo.getRequestCommand())
        {
            Log.d(TAG,msg.obj.toString());
            editTextSignalValue.setText(msg.obj.toString());
        }else if(msg.what==SocketMessageType.GetSignalDi.getRequestCommand())
        {
            Log.d(TAG,msg.obj.toString() + ":" + msg.arg1);
            editTextSignalValue.setText(Integer.toString( msg.arg1));
        }else if(msg.what==SocketMessageType.GetSignalGi.getRequestCommand())
        {
            Log.d(TAG,msg.obj.toString() + ":" + msg.arg1);
            editTextSignalValue.setText(Integer.toString( msg.arg1));
        }else if(msg.what==SocketMessageType.GetSignalAi.getRequestCommand())
        {
            Log.d(TAG,msg.obj.toString());
            editTextSignalValue.setText(msg.obj.toString());
        }


    }
}
