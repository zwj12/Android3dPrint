package com.example.android3dprint.robot;

import android.util.Log;

import java.io.PrintWriter;

public class SocketSendService implements Runnable {
    private static final String TAG = "SocketHandler";
    private PrintWriter printWriter;
    private String msg;

    public SocketSendService(String msg, PrintWriter printWriter) {
        this.msg = msg;
        this.printWriter=printWriter;
    }

    @Override
    public void run() {
        Log.d(TAG,msg);
        if(printWriter==null)
        {
            Log.d(TAG,"printWriter==null");
        }else
        {
            printWriter.println(this.msg);
        }

    }
}
