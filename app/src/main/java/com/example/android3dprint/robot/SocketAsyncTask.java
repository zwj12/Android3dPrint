package com.example.android3dprint.robot;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

import com.example.android3dprint.MainActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketAsyncTask extends AsyncTask<SocketMessageType, Integer, SocketMessageType[]> {
    private static final String TAG = "SocketAsyncTask";
    private static final int connectTimeOut = 10000;
    private static final int soTimeOut = 0;

    private static String HOST = "10.0.2.2";
    private static int PORT = 3003;
    private static Socket socket =null;
    private static OutputStream outputStream;
    private static InputStream inputStream;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;

    private OnSocketListener socketListener;

    public SocketAsyncTask(String HOST, int PORT, OnSocketListener socketListener) {
        SocketAsyncTask.HOST = HOST;
        SocketAsyncTask.PORT = PORT;
        this.socketListener = socketListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(TAG, "onPreExecute in UI thread");
    }

    @Override
    protected SocketMessageType[] doInBackground(SocketMessageType... socketMessageTypes) {

        try {
            connectToRobot();
            byte[] receiveBytes = new byte[1024];
            for (int i = 0; i < socketMessageTypes.length; i++) {
                dataOutputStream.write(socketMessageTypes[i].getRequestRawBytes());
                dataOutputStream.flush();
                dataInputStream.read(receiveBytes, 0, 3);
                dataInputStream.read(receiveBytes, 3, (receiveBytes[1] << 8) + receiveBytes[2]);
                socketMessageTypes[i].unpackResponseRawBytes(receiveBytes);
                publishProgress(i * 100 / socketMessageTypes.length);
                if(socketMessageTypes[i]==SocketMessageType.CloseConnection){
                    socket.close();
                    socket=null;
                    break;
                }
            }
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
        Log.d(TAG, String.format("doInBackground in SocketAsyncTask thread"));
        return socketMessageTypes;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        Log.d(TAG, String.format("onProgressUpdate in UI thread, %d", progress[0]));
    }

    @Override
    protected void onPostExecute(SocketMessageType[] socketMessageTypes) {
        super.onPostExecute(socketMessageTypes);
        for (SocketMessageType socketMessageType : socketMessageTypes) {
            if(socketMessageType!=null){
                Log.d(TAG, String.format("onPostExecute in UI thread, %s", socketMessageType.responseValue));
            }
        }
        this.socketListener.refreshUI(socketMessageTypes);
    }

    private void connectToRobot() throws IOException {
        if (socket==null || !socket.isConnected() || socket.isClosed() ) {
            socket=new Socket();
            SocketAddress endpoint = new InetSocketAddress(HOST, PORT);
            socket.connect(endpoint, connectTimeOut);
            socket.setSoTimeout(soTimeOut);
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
            dataInputStream = new DataInputStream(new BufferedInputStream(inputStream));
            dataOutputStream = new DataOutputStream(new BufferedOutputStream(outputStream));
            Log.d(TAG, "The robot is connected");
        }else
        {
            Log.d(TAG, "The robot is already connected");
        }

    }

    public interface OnSocketListener {
        void refreshUI(SocketMessageType[] socketMessageTypes);
    }

}
