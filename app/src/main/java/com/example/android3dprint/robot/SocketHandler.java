package com.example.android3dprint.robot;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class SocketHandler extends Handler {
    private static final String TAG = "SocketHandler";

    private TextView textView;

    public SocketHandler(TextView textView){
        this.textView=textView;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                Log.e(TAG, msg.obj.toString());
                this.textView.setText(msg.obj.toString());
                break;
        }
    }
}
