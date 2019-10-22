package com.example.android3dprint.robot;

import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class SocketSendService implements Runnable {
    private static final String TAG = "SocketSendService";
    private SocketHandler socketHandler;

    public SocketSendService(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    @Override
    public void run() {
        try {
            if (this.socketHandler.isRawData) {
                Log.d(TAG,"Send RawData");
                this.socketHandler.outputStream.write(this.socketHandler.sendBytes,0,this.socketHandler.sendBytesLength);
                socketHandler.receiveBytesLength = socketHandler.inputStream.read(socketHandler.receiveBytes);
                if (socketHandler.receiveBytesLength != -1) {
                    Message msg = Message.obtain();
                    msg.what = socketHandler.receiveBytes[0];
                    msg.arg1 = socketHandler.receiveBytes[1] + (socketHandler.receiveBytes[2] << 8);
                    msg.obj = socketHandler.receiveBytes;
                    socketHandler.sendMessage(msg);
                    Log.d(TAG,socketHandler.receiveBytes.toString());
                }
            }
            else
            {
                this.socketHandler.printWriter.println(this.socketHandler.sendMsg);
            }
        }
        catch (Exception e){

        }

    }
}
